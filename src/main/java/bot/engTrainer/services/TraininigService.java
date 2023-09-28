package bot.engTrainer.services;

import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.Dictionaries;
import bot.engTrainer.entities.TrainingBuffer;
import bot.engTrainer.entities.Words;
import bot.engTrainer.entities.dto.TrainingBufferDto;
import bot.engTrainer.entities.dto.TrainingProgressDto;
import bot.engTrainer.entities.mappers.TrainingBufferMapper;
import bot.engTrainer.entities.mappers.WordsMapper;
import bot.engTrainer.helpers.*;
import bot.engTrainer.repository.TrainingBufferRepository;
import bot.engTrainer.repository.TrainingProgressRepository;
import com.pengrad.telegrambot.model.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraininigService {

    private final TrainingBufferRepository trainingBufferRepository;
    private final TrainingProgressRepository trainingProgressRepository;
    private final TrainingBufferMapper trainingBufferMapper;
    private final BotUserService botUserService;
    private final DictionaryService dictionaryService;
    private final WordsMapper wordsMapper;

    public Set<TrainingBufferDto> getTrainingBuffer(Long UserId){
        return trainingBufferRepository.findByUserId(UserId).stream().map(trainingBufferMapper::toDto).collect(Collectors.toSet());
    }

    private boolean timeToShow(TrainingProgressDto curProgress){
        boolean retVal = false;
        if(curProgress.getProgress()<3){
            java.util.Date utilDate = new java.util.Date();
            long diffInMillies = Math.abs(curProgress.getLastAppearanceDate().getTime() - (new java.sql.Date(utilDate.getTime())).getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long diffInHours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if(curProgress.getLearningInterval() == TrainingIntervals.WEEKS_2.ordinal()){
                if(diffInDays<=14){
                    retVal = true;
                }
            } else if (curProgress.getLearningInterval() == TrainingIntervals.HOURS_24.ordinal()) {
                if(diffInHours<=24){
                    retVal = true;
                }
            } else if (curProgress.getLearningInterval() == TrainingIntervals.HOURS_8.ordinal()) {
                if(diffInHours<=8){
                    retVal = true;
                }
            } else if (curProgress.getLearningInterval() == TrainingIntervals.MINUTES_15.ordinal()) {
                if(diffInMinutes<=15){
                    retVal = true;
                }
            }
        }
        return retVal;
    }

    public void prepareNewTraining(Chat chat, TrainingModes mode){

        //1. peek words for repeat (2 weeks)
        //2. peek words for repeat (24-32 hors)
        //3. peek words for repeat (8-12 hors)
        //4. peek words for repeat (15-20 minutes)
        //5. random seek for learning

        Set<Dictionaries> dictionaries = botUserService.getSelectedDictionaries(chat);
        BotUser user = botUserService.getUserByChat(chat);
        int intensity = user.getTrainigIntensity(); //count of words for 1 training

        trainingBufferRepository.deleteByUserId(user.getId());
        Set<TrainingBufferDto> buffer = new HashSet<>();
        Set<TrainingProgressDto> progress = trainingProgressRepository.findByUserId(chat.id());

        if(mode==TrainingModes.LEARN || mode==TrainingModes.TRAINING){

            int trainBufferLimit = intensity;
            int learnBufferLimit = intensity;

            for(TrainingProgressDto curProgress: progress){
                if(curProgress.getProgress()<3){
                    if(this.timeToShow(curProgress)){
                        trainBufferLimit--;
                        buffer.add(TrainingBufferDto.builder()
                                        .word(curProgress.getWord())
                                        .wordState(WordState.EXAM_WITH_VARIANTS.ordinal())
                                        .progress(curProgress.getProgress())
                                .build());
                    }
                }
                if(trainBufferLimit<=0){
                    break;
                }
            }

            List<Words> newWords = trainingProgressRepository.getNewWordsByDictionary(dictionaries);
            Collections.shuffle(newWords);
            for(Words curWord: newWords){
                learnBufferLimit--;
                buffer.add(TrainingBufferDto.builder()
                        .word(wordsMapper.toDto(curWord))
                        .wordState(WordState.NEW.ordinal())
                        .progress(0)
                        .build());
                if(learnBufferLimit<=0){
                    break;
                }
            }

        }

        for (TrainingBufferDto bufferElement: buffer){
            trainingBufferRepository.save(trainingBufferMapper.toModel(bufferElement));
        }

    }

    public boolean isNextWord(Set<TrainingBufferDto> trainingBufferDtoSet){
        if(trainingBufferDtoSet.size()>0) {
              return true;
        }
        return false;
    }

    public boolean isNextNewWord(Set<TrainingBufferDto> trainingBufferDtoSet){
        for (TrainingBufferDto tbDto: trainingBufferDtoSet) {
            if(tbDto.getWordState() == WordState.NEW.ordinal()){
                return true;
            }
        }
        return false;
    }

    public boolean isNextExamWord(Set<TrainingBufferDto> trainingBufferDtoSet){
        for (TrainingBufferDto tbDto: trainingBufferDtoSet) {
            if(tbDto.getWordState() != WordState.NEW.ordinal()){
                return true;
            }
        }
        return false;
    }

    public TrainingSummary getSummary() {
        return TrainingSummary.builder()
                .newWords(10)
                .studyWords(10)
                .build();
    }

    public void goToNextWord(Chat chat) {

        //got to next new word which didn't has been processed yet
        //if such word didn't find out, look for exam word which not has been processed yet
        //if such word didn't find out, look for exam words with progress < 1

    }

    public void acceptCorrectAnswer(Chat chat) {

        //find current word (flag inProcess is set )
        //increase the 'progress' field by one

    }

    public void acceptWrongAnswer(Chat chat) {

        //find current word (flag inProcess is set )
        //decrease the 'progress' field by one

    }

    public NextWord getNextExamWord(Chat chat, Set<TrainingBufferDto> trainingBufferDtoSet) {
        TrainingBufferDto selectedWord = null;
        for (TrainingBufferDto tbDto: trainingBufferDtoSet) {
            if(tbDto.getWordState() != WordState.NEW.ordinal()){
                selectedWord = tbDto;
            }
        }
        if(selectedWord==null){
            return null;
        }
        return NextWord.builder()
                .foreignWriting(selectedWord.getWord().getForeignWrite())
                .transcription(selectedWord.getWord().getTranscription())
                .description(selectedWord.getWord().getDescription())
                .variants(trainingBufferRepository.getAnswerVariants(chat.id()))
                .build();
    }

    public NextWord getNextNewWord(Chat chat, Set<TrainingBufferDto> trainingBufferDtoSet) {
        TrainingBufferDto selectedWord = null;
        for (TrainingBufferDto tbDto: trainingBufferDtoSet) {
            if(tbDto.getWordState() != WordState.NEW.ordinal()){
                selectedWord = tbDto;
            }
        }
        return NextWord.builder()
                .foreignWriting(selectedWord.getWord().getForeignWrite())
                .transcription(selectedWord.getWord().getTranscription())
                .description(selectedWord.getWord().getDescription())
                .build();
    }
}
