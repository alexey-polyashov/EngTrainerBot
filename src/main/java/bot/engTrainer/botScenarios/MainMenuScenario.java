package bot.engTrainer.botScenarios;

import bot.engTrainer.exceptions.SWUException;
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


public class MainMenuScenario extends CommonScenario<String, StageParams> {

    private ScenarioService scenarioService;
    private BotService botService;

    static final String msg_settings = "Settings";
    static final String msg_settings_cmd = "/settings";
    static final String msg_select_dictionary = "Select dictionary";
    static final String msg_select_dictionary_cmd = "/dictionary";
    static final String msg_start_training = "Start training";
    static final String msg_start_training_cmd = "/training";

    static final String msg_settings_training_time = "Intervals";
    static final String msg_settings_training_time_cmd = "/intervals";

    public MainMenuScenario() {
        super();
        setScenarioId("MainMenuScenario");
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
                    new KeyboardButton(msg_start_training),
                    new KeyboardButton(msg_select_dictionary),
                    new KeyboardButton(msg_settings))
                    .oneTimeKeyboard(true)   // optional
                    .resizeKeyboard(true)    // optional
                    .selective(true);        // optional
            bot.execute(new SendMessage(chat.id(),"").replyMarkup(keyboard));

            return "2";
        });

        SimpleScenarioStage<String, StageParams> st2 = new SimpleScenarioStage<>("2", (p) -> {
            //if something wrong, return here
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            if(mes.equals(msg_settings) || mes.equals(msg_settings_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы вошли в настройки."));
                bot.execute(new SendMessage(chat.id(), "Выберите в меню пункт для настроек"));
                return "3";
            }else if(mes.equals(msg_select_dictionary) || mes.equals(msg_select_dictionary_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы вошли в меню выбора словарей. Выберите аункт в меню для настройки словарей"));
                return "4";
            }else if(mes.equals(msg_start_training) || mes.equals(msg_start_training_cmd)){
                bot.execute(new SendMessage(chat.id(), "Тренировка начинается"));
                return "5";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню и действуюте по инструкциям."));
                goToStage("1");
                doWork(p);
                return "1";
            }

        });

        SimpleScenarioStage<String, StageParams> st3 = new SimpleScenarioStage<>("3", (p) -> {
            //if something wrong, return here
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            if(mes.equals(msg_settings) || mes.equals(msg_settings_cmd)){
                bot.execute(new SendMessage(chat.id(), "Выберите в меню пункт для настроек"));
                return "3";
            }else if(mes.equals(msg_select_dictionary) || mes.equals(msg_select_dictionary_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы вошли в меню выбора словарей. Выберите аункт в меню для настройки словарей"));
                return "4";
            }else if(mes.equals(msg_start_training) || mes.equals(msg_start_training_cmd)){
                bot.execute(new SendMessage(chat.id(), "Тренировка начинается"));
                return "5";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню и действуюте по инструкциям."));
                goToStage("1");
                doWork(p);
                return "1";
            }

        });

        addStage(st1);
        addStage(st2);

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
