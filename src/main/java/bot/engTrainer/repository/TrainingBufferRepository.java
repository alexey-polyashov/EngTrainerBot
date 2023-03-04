package bot.engTrainer.repository;

import bot.engTrainer.entities.TrainingBuffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrainingBufferRepository extends JpaRepository<TrainingBuffer, Long> {

}
