package bot.engTrainer.repository;


import bot.engTrainer.entities.ScenarioModel;
import bot.engTrainer.entities.ScenarioModelPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScenarioRepository extends JpaRepository<ScenarioModel, ScenarioModelPK> {

    Optional<ScenarioModel> findByChatIdAndIdentifier(Long chatId, String identifier);
    List<ScenarioModel> findByChatId(Long chatId);

    long deleteByChatId(Long id);
}
