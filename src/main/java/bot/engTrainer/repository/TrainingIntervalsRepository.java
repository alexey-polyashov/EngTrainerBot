package bot.engTrainer.repository;

import bot.engTrainer.entities.TrainingIntervals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrainingIntervalsRepository extends JpaRepository<TrainingIntervals, Long> {

}
