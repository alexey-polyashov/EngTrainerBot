package bot.engTrainer.scenariodefine.simplescenario;

import bot.engTrainer.scenariodefine.ScManager;
import bot.engTrainer.scenariodefine.Scenario;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
public class SimpleScManager<T, P> implements ScManager<T, P> {

    Map<String, Class> scMap = new HashMap<>();

    @Override
    public Optional<Scenario<T, P>> getScenarioById(String id) {
        Class scClass = scMap.get(id);
        if(scClass!=null){
            try {
                return Optional.of((Scenario<T, P>)scClass.getConstructor().newInstance());
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
        return Optional.empty();
    }

    @Override
    public void attach(Class scenario, String id) {
        scMap.put(id, scenario);
    }

    @Override
    public void detach(String id) {
        scMap.remove(id);
    }
}
