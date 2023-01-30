package bot.engTrainer.services;

import bot.engTrainer.botScenarios.StageParams;
import bot.engTrainer.entities.ScenarioModel;
import bot.engTrainer.repository.ScenarioRepository;
import bot.engTrainer.scenariodefine.Scenario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;

    public String restoreScenario(Long UserId, Scenario<String, StageParams> scenario){
        Optional<ScenarioModel> sc = scenarioRepository.findByChatIdAndIdentifier(UserId, scenario.getId());
        if(sc.isPresent()){
            return sc.get().getData();
        }else{
            return "";
        }
    }

    public Long saveScenario(Long UserId, Scenario<String, StageParams> scenario, String data){
        Optional<ScenarioModel> sc = scenarioRepository.findByChatIdAndIdentifier(UserId, scenario.getId());
        ScenarioModel newScenario = new ScenarioModel();
        if(sc.isEmpty()){
            newScenario.setChatId(UserId);
            newScenario.setIdentifier(scenario.getId());
        }else{
            newScenario = sc.get();
        }
        newScenario.setData(data);
        scenarioRepository.save(newScenario);
        return UserId;
    }

    public void deleteByChatId(Long id) {
        scenarioRepository.deleteByChatId(id);
    }
}
