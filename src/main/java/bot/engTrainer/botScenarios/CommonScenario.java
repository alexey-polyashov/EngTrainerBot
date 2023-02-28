package bot.engTrainer.botScenarios;

import bot.engTrainer.exceptions.BotException;
import bot.engTrainer.scenariodefine.simplescenario.SimpleScenario;
import bot.engTrainer.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class CommonScenario extends SimpleScenario<String, StageParams> {

    protected ScenarioService scenarioService;
    protected BotService botService;
    protected BotUserService botUserService;
    protected DictionaryService dictionaryService;
    protected TraininigService traininigService;

    public void setServices(BotService botService) {
        this.botService = botService;
        this.scenarioService = botService.getScenarioService();
        this.botUserService = botService.getBotUserService();
        this.dictionaryService = botService.getDictionaryService();
    }

    @Override
    public void finish() {
        botService.endCurrentScenario(botService.getCurrentChat());
    }

    @Override
    public String toString() {
        return "{" +
                "\"currentStage\":\"" + getCurrentStage().getIdentifier() + "\"," +
                "\"started\":\"" + isStarted() + "\"," +
                "\"done\":\"" + isDone() + "\"" +
                "}";
    }

    @Override
    public long save() {
        String jsonData = toString();
        return scenarioService.saveScenario(botService.getCurrentChat().id(),this,  jsonData);
    }

    @Override
    public void load(long id) {
        String jsonData = "";
        Map<String, String> mapped = new HashMap<>();
        jsonData = scenarioService.restoreScenario(botService.getCurrentChat().id(), this);
        if(!jsonData.isEmpty()){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                mapped = objectMapper.readValue(jsonData, Map.class);
            } catch (JsonProcessingException e) {
                throw new BotException("Ошибка инициализации сценария" + getId());
            }
            String stageKey = mapped.get("currentStage");
            setCurrentStage(getStage(stageKey).orElseThrow(()->new BotException("Не определен этап сценария " + stageKey)));
            setDone(Boolean.valueOf(mapped.get("done")));
            setStarted(Boolean.valueOf(mapped.get("started")));
        }
    }

}
