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

@Service
@RequiredArgsConstructor
public class TraininigService {

    private final TrainingBufferRepository trainingBufferRepository;
    private final TrainingProgressRepository trainingProgressRepository;
    private final TrainingBufferMapper trainingBufferMapper;
    private final BotUserService botUserService;
    private final DictionaryService dictionaryService;
    private final WordsMapper wordsMapper;

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

    public boolean isNextNewWord(){
        return true;
    }

    public boolean isNextExamWord(){
        return true;
    }

    public TrainingSummary getSummary() {
        return TrainingSummary.builder()
                .newWords(10)
                .studyWords(10)
                .build();
    }

    public void goToNextWord(Chat chat) {

    }

    public void acceptCorrectAnswer(Chat chat) {

    }

    public void acceptWrongAnswer(Chat chat) {

    }

    public NextWord getNextExamWord(Chat chat) {
        TrainingBuffer chosenWord = trainingBufferRepository.getNextExamWord();
        return NextWord.builder()
                .foreignWriting(chosenWord.getWord().getForeignWrite())
                .transcription(chosenWord.getWord().getTranscription())
                .description(chosenWord.getWord().getDescription())
                .variants(trainingBufferRepository.getAnswerVariants(chosenWord.getWord()))
                .build();
    }

    public NextWord getNextNewWord(Chat chat) {
        TrainingBuffer chosenWord = trainingBufferRepository.getNextNewWord();
        return NextWord.builder()
                .foreignWriting(chosenWord.getWord().getForeignWrite())
                .transcription(chosenWord.getWord().getTranscription())
                .description(chosenWord.getWord().getDescription())
                .build();
    }
}
