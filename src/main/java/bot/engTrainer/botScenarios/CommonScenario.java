package bot.engTrainer.botScenarios;

import bot.engTrainer.scenariodefine.simplescenario.SimpleScenario;
import bot.engTrainer.services.BotService;
import bot.engTrainer.services.ScenarioService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CommonScenario<T, P> extends SimpleScenario<T, P> {

    private ScenarioService scenarioService;
    private BotService botService;

}
