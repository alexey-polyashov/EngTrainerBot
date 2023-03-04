package bot.engTrainer.services;

import bot.engTrainer.entities.*;
import bot.engTrainer.exceptions.ResourceNotFound;
import bot.engTrainer.repository.DictionaryRepository;
import bot.engTrainer.repository.WordsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final WordsRepository wordsRepository;

    public List<Dictionaries> getDictionaries(){
        return dictionaryRepository.findAll();
    }

    public Dictionaries getDictionaryByName(String name){
        return dictionaryRepository.findByName(name).orElseThrow(()->new ResourceNotFound("Словаь по имени '" + name + "' не найден"));
    }

    public List<Words> getWords(Dictionaries dict){
        return wordsRepository.findByDictionary(dict);
    }

    public void delWord(Dictionaries dict, Words word){

    }
    public void addWord(Dictionaries dict, Words word){

    }

    public List<Words> getWordsInDictionary(Dictionaries dict){
        List<Words> words = wordsRepository.findByDictionary(dict);
        return words;
    }

    public Dictionaries getById(long id) {
        return dictionaryRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Словаь по id '" + id + "' не найден"));
    }
}
