package bot.engTrainer.repository;

import bot.engTrainer.entities.TrainingBuffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface TrainingBufferRepository extends JpaRepository<TrainingBuffer, Long> {

    void deleteByUserId(Long userId);
    Set<TrainingBuffer> findByUserId(Long userId);

    @Query
    TrainingBuffer getNextExamWord(Long userId);

    Set<String> getAnswerVariants(Long userId);

    TrainingBuffer getNextNewWord(Long userId);
}
