package bot.engTrainer.repository;

import bot.engTrainer.entities.TrainingBuffer;
import bot.engTrainer.entities.dto.TrainingBufferDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface TrainingBufferRepository extends JpaRepository<TrainingBuffer, Long> {

    void deleteByUserId(Long userId);
    Set<TrainingBufferDto> findByUserId(Long userId);

}
