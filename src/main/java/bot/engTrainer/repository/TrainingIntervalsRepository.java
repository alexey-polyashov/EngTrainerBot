package bot.engTrainer.repository;

import bot.engTrainer.entities.TrainingIntervals;
import bot.engTrainer.entities.dto.TrainingIntervalsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface TrainingIntervalsRepository extends JpaRepository<TrainingIntervals, Long> {
    Set<TrainingIntervalsDto> findByUserId(Long userId);
}
