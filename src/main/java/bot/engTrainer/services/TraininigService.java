package bot.engTrainer.services;

import bot.engTrainer.helpers.NextWord;
import com.pengrad.telegrambot.model.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TraininigService {

    public void prepareNewTraining(Chat chat){

    }

    public void startTraining(Chat chat){

    }

    public void stopTraining(){

    }

    public boolean isTrainingActive(Chat chat){
        return false;
    }

    public NextWord getNextWord(Chat chat){
        return null;
    }

    public boolean isNextWord(Chat chat){
        return false;
    }

    public void acceptAnswer(Chat chat){

    }

    public boolean newWordsContains(){
        return true;
    }

    public void learnNewWord(Chat chat) {

    }
}
