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
        this.attach(MyDictionarySetupScenario.class, "MyDictionarySetupScenario");
        this.attach(SettingsScenario.class, "SettingsScenario");
        this.attach(MainMenuScenario.class, "MainMenuScenario");
    }

}
