package bot.engTrainer.botScenarios;

import bot.engTrainer.scenariodefine.simplescenario.SimpleScManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SimpleScManagerConfig extends SimpleScManager<String, StageParams> {

    @PostConstruct
    private void init(){
        this.attach(NewUserConnectedScenario.class, "NewUserConnectedScenario");
        this.attach(TrainingScenario.class, "TrainingScenario");
        this.attach(TrainingScenario.class, "MyDictionarySetupScenario");
        this.attach(TrainingScenario.class, "SettingsScenario");
        this.attach(TrainingScenario.class, "MainMenuScenario");
    }

}
