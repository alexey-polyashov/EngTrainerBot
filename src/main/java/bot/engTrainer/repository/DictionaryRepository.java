package bot.engTrainer.repository;

import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.Dictionaries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionaries, Long> {

    List<Dictionaries> findAll();
    Optional<Dictionaries> findByName(String name);

}
