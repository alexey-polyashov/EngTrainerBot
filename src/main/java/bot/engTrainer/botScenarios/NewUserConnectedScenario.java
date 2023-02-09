package bot.engTrainer.botScenarios;

import bot.engTrainer.exceptions.SWUException;
import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
import bot.engTrainer.services.BotService;
import bot.engTrainer.services.BotUserService;
import bot.engTrainer.services.ScenarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;
import java.util.Map;


public class NewUserConnectedScenario extends CommonScenario<String, StageParams> {

    private ScenarioService scenarioService;
    private BotService botService;
    private BotUserService botUserService;

    public NewUserConnectedScenario() {
        super();
        setScenarioId("NewUserConnectedScenario");
    }

    public void setBotUserService(BotUserService botUserService) {
        this.botUserService = botUserService;
    }

    public void setScenarioService(ScenarioService scenarioService){
        this.scenarioService = scenarioService;
    }

    public void setBotService(BotService botService){
        this.botService = botService;
    }

    @Override
    public void init() {

        SimpleScenarioStage<String, StageParams> st1 = new SimpleScenarioStage<>("1", (p) -> {
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            bot.execute(new SendMessage(chat.id(), "Добро пожаловать в чат для тренировки английского!"));
            bot.execute(new SendMessage(chat.id(), "Для начала необходимо пройти простую процедуру регистрации!"));
            return "2";
        });

        SimpleScenarioStage<String, StageParams> st2 = new SimpleScenarioStage<>("2", (p) -> {
            //if something wrong, return here
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            bot.execute(new SendMessage(chat.id(), "Введите свое имя, как к вам можно обращаться"));
            return "3";
        });

        SimpleScenarioStage<String, StageParams> st3 = new SimpleScenarioStage<>("3", (p) -> {
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String userName = p.getMessage().text();
            bot.execute(new SendMessage(chat.id(), "Выполняю регистрацию нового пользователя ..."));
            try {
                botUserService.registerNewUser(chat, userName.trim(), "");
                return null;
            }catch(Exception e){
                bot.execute(new SendMessage(chat.id(), "Призошла ошибка. Повторите ввод, или напишите разработчикам polyashofff@yandex.ru"));
                return "2";
            }
        });

        addStage(st1);
        addStage(st2);
        addStage(st3);

    }

    @Override
    public void finish() {
        botService.endCurrentScenario();
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
                throw new SWUException("Ошибка инициализации сценария" + getId());
            }
            String stageKey = mapped.get("currentStage");
            setCurrentStage(getStage(stageKey).orElseThrow(()->new SWUException("Не определен этап сценария " + stageKey)));
            setDone(Boolean.valueOf(mapped.get("done")));
            setStarted(Boolean.valueOf(mapped.get("started")));
        }
    }

}
