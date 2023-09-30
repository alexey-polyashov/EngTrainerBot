package bot.engTrainer.repository;

import bot.engTrainer.entities.Dictionaries;
import bot.engTrainer.entities.Words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface WordsRepository extends JpaRepository<Words, Long> {

    List<Words> findByDictionary(Dictionaries dict);

    @Query("Select w" +
            "From BotUser bu w JOIN bu.dictionaries d JOIN d.dictWords w" +
            "Where bu.chatId  = ?1" +
            "Order by random()" +
            "Limit ?2"
    )
    Set<Words> findRandomByDictionary(Long chatId, int limit);

}
