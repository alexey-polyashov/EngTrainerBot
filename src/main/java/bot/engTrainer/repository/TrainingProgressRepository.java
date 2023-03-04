package bot.engTrainer.repository;

import bot.engTrainer.entities.TrainingProgress;
import bot.engTrainer.entities.dto.TrainingProgressDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface TrainingProgressRepository extends JpaRepository<TrainingProgress, Long> {

    Set<TrainingProgressDto> findByUserId(Long userId);

}
