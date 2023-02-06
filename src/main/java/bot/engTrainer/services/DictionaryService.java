package bot.engTrainer.services;

import bot.engTrainer.entities.*;
import bot.engTrainer.exceptions.ResourceNotFound;
import bot.engTrainer.repository.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public List<Dictionaries> getDictionaries(){
        return dictionaryRepository.findAll();
    }

    public Dictionaries getDictionaryByName(String name){
        return dictionaryRepository.findByName(name).orElseThrow(()->new ResourceNotFound("Словаь по имени '" + name + "' не найден"));
    }

    public Optional<BotUser> getWords(Dictionaries dict){
        return null;
    }

    public void delWord(Dictionaries dict, Words word){

    }
    public void addWord(Dictionaries dict, Words word){

    }

    public Dictionaries getById(long id) {
        return dictionaryRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Словаь по id '" + id + "' не найден"));
    }
}
