package bot.engTrainer.services;

import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.Dictionaries;
import bot.engTrainer.entities.Words;
import bot.engTrainer.entities.dto.TrainingProgressDto;
import bot.engTrainer.helpers.NextWord;
import bot.engTrainer.helpers.TrainingModes;
import bot.engTrainer.helpers.TrainingSummary;
import bot.engTrainer.repository.TrainingBufferRepository;
import bot.engTrainer.repository.TrainingProgressRepository;
import com.pengrad.telegrambot.model.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TraininigService {

    private final TrainingBufferRepository trainingBufferRepository;
    private final TrainingProgressRepository trainingProgressRepository;
    private final BotUserService botUserService;
    private final DictionaryService dictionaryService;

    public void prepareNewTraining(Chat chat, TrainingModes mode){

        Set<Dictionaries> dictionaries = botUserService.getSelectedDictionaries(chat);
        BotUser user = botUserService.getUserByChat(chat);
        int intensity = user.getTrainigIntensity();

        Map<Dictionaries, List<Words>> wordsCash =new HashMap<>();
        Set<TrainingProgressDto> progress = trainingProgressRepository.findByUserId(chat.id());

        if(mode==TrainingModes.LEARN || mode==TrainingModes.TRAINING){
            for (Dictionaries dict: dictionaries){
                List<Words> words = new LinkedList<>();
                if(wordsCash.containsValue(dict)){
                    words = wordsCash.get(dict);
                }else{
                    words = dictionaryService.getWords(dict);
                }



            }
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
        return null;
    }

    public NextWord getNextNewWord(Chat chat) {
        return null;
    }
}
