package bot.engTrainer.repository;

import bot.engTrainer.entities.Dictionaries;
import bot.engTrainer.entities.Words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WordsRepository extends JpaRepository<Words, Long> {
    List<Words> findByDictionary(Dictionaries dict);
}
