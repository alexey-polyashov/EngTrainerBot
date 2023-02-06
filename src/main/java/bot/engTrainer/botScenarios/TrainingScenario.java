package bot.engTrainer.botScenarios;

import bot.engTrainer.exceptions.SWUException;
import bot.engTrainer.scenariodefine.Scenario;
import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
import bot.engTrainer.services.BotService;
import bot.engTrainer.services.ScenarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;
import java.util.Map;


public class TrainingScenario extends CommonScenario<String, StageParams> {

    static final String msg_help = "Help";
    static final String msg_help_cmd = "/help";
    static final String msg_mainmenu = "Later";
    static final String msg_mainmenu_cmd = "/later";
    static final String msg_training_ready = "Start";
    static final String msg_training_ready_cmd = "/start";


    private ScenarioService scenarioService;
    private BotService botService;

    public TrainingScenario() {
        super();
        setScenarioId("TrainingScenario");
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

            Keyboard keyboard = new ReplyKeyboardMarkup(
                    new KeyboardButton(msg_training_ready),
                    new KeyboardButton(msg_help),
                    new KeyboardButton(msg_mainmenu))
                    .oneTimeKeyboard(true)   // optional
                    .resizeKeyboard(true)    // optional
                    .selective(true);        // optional
            bot.execute(new SendMessage(chat.id(),"Тренировка займет некоторое время."));
            bot.execute(new SendMessage(chat.id(),"Готов?").replyMarkup(keyboard));
            return "2";
        });

        SimpleScenarioStage<String, StageParams> st2 = new SimpleScenarioStage<>("2", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            switch (mes) {
                case msg_training_ready:
                case msg_training_ready_cmd:
                    goToStage("30");
                    doWork(p);
                    return "30";
                case msg_mainmenu:
                case msg_mainmenu_cmd:
                    bot.execute(new SendMessage(chat.id(), "Вы вернулись в главное меню"));
                    return null;
                case msg_help:
                case msg_help_cmd:
                    bot.execute(new SendMessage(chat.id(), "Вы находитесь в разделе тренировки"));
                    bot.execute(new SendMessage(chat.id(), "Вы можете начать тренировку, или отложить ее."));
                    bot.execute(new SendMessage(chat.id(), "Во время начала тренировки бот подберет слова из подключенных словарей, исходя из выбранной интенсивности тренировок и результатов предыдущих тренировок. "));
                    bot.execute(new SendMessage(chat.id(), "Старайтесь отвечать обдуманно. После тренировки, бот запомнит результаты ответов."));
                    return "2";
                default:
                    bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню."));
                    goToStage("1");
                    doWork(p);
                    return "1";
            }

        });

        SimpleScenarioStage<String, StageParams> st30 = new SimpleScenarioStage<>("30", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            bot.execute(new SendMessage(chat.id(), "Тренировка закончена"));
            return null;

        });
        addStage(st1);
        addStage(st2);
        addStage(st30);

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
