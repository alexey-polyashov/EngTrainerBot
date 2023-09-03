package bot.engTrainer.repository;

import bot.engTrainer.entities.Dictionaries;
import bot.engTrainer.entities.TrainingProgress;
import bot.engTrainer.entities.Words;
import bot.engTrainer.entities.dto.TrainingProgressDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface TrainingProgressRepository extends JpaRepository<TrainingProgress, Long> {

    Set<TrainingProgressDto> findByUserId(Long userId);
    void deleteByUserId(Long userId);

    @Query(
            value="SELECT w FROM Words w LEFT JOIN TrainingProgress p WHERE w.dictionary in (?1) AND w = p.word AND p IS NULL"
    )
    List<Words> getNewWordsByDictionary(Set<Dictionaries> dictionaries);
}
