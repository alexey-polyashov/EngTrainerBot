package bot.engTrainer.botScenarios;

import bot.engTrainer.entities.TrainingIntervals;
import bot.engTrainer.exceptions.SWUException;
import bot.engTrainer.scenariodefine.Scenario;
import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
import bot.engTrainer.services.BotService;
import bot.engTrainer.services.BotUserService;
import bot.engTrainer.services.ScenarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class SettingsScenario extends CommonScenario<String, StageParams> {

    private ScenarioService scenarioService;
    private BotService botService;
    private BotUserService botUserService;

    static final String msg_settings_training_time = "Training intervals";
    static final String msg_settings_training_time_cmd = "/intervals";
    static final String msg_settings_intensive = "Intensity";
    static final String msg_settings_intensive_cmd = "/intensity";
    static final String msg_help = "Help";
    static final String msg_help_cmd = "/help";
    static final String msg_settings_mainmenu = "Main menu";
    static final String msg_settings_mainmenu_cmd = "/mainmenu";
    static final String msg_settings_back = "Back";
    static final String msg_settings_back_cmd = "/back";

    static final String msg_settings_training_time_show = "Show intervals";
    static final String msg_settings_training_time_show_cmd = "/show";
    static final String msg_settings_training_time_add = "Add interval";
    static final String msg_settings_training_time_add_cmd = "/add";
    static final String msg_settings_training_time_del = "Delete interval";
    static final String msg_settings_training_time_del_cmd = "/del";
    static final String msg_settings_training_time_clr = "Clear intervals";
    static final String msg_settings_training_time_clr_cmd = "/clear";
    static final String msg_settings_yes = "yes";


    public SettingsScenario() {
        super();
        setScenarioId("SettingsScenario");
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
                    new KeyboardButton(msg_settings_training_time),
                    new KeyboardButton(msg_settings_intensive),
                    new KeyboardButton(msg_help),
                    new KeyboardButton(msg_settings_mainmenu))
                    .oneTimeKeyboard(true)   // optional
                    .resizeKeyboard(true)    // optional
                    .selective(true);        // optional
            bot.execute(new SendMessage(chat.id(),"Вы находитесь в разделе настроек").replyMarkup(keyboard));

            return "2";

        });

        SimpleScenarioStage<String, StageParams> st2 = new SimpleScenarioStage<>("2", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            if(mes.equals(msg_settings_training_time) || mes.equals(msg_settings_training_time_cmd)){
                Scenario<String, StageParams> sc = botService.startScenario("SettingsScenario", chat);
                sc.doWork(p);
                botService.checkScenStack();
                return "30";
            }else if(mes.equals(msg_settings_intensive) || mes.equals(msg_settings_intensive_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы вошли в меню выбора словарей. Выберите аункт в меню для настройки словарей"));
                return "40";
            }else if(mes.equals(msg_settings_mainmenu) || mes.equals(msg_settings_mainmenu_cmd)){
                bot.execute(new SendMessage(chat.id(), "Тренировка начинается"));
                return "50";
            }else if(mes.equals(msg_help) || mes.equals(msg_help_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы находитесь в разделе настроек"));
                bot.execute(new SendMessage(chat.id(), "Здесь вы можете настроить временные интервалы тренировок. Бот запустит тренировку автоматически в выбранные интервалы времени. "));
                bot.execute(new SendMessage(chat.id(), "Можете настроить интенсивность - количество слов за одну тренировку"));
                return "2";
            }else{
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

            Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(msg_settings_training_time_show),
                new KeyboardButton(msg_settings_training_time_add),
                new KeyboardButton(msg_settings_training_time_del),
                new KeyboardButton(msg_settings_training_time_clr),
                new KeyboardButton(msg_help),
                new KeyboardButton(msg_settings_back)
            )
            .oneTimeKeyboard(true)   // optional
            .resizeKeyboard(true)    // optional
            .selective(true);        // optional
            bot.execute(new SendMessage(chat.id(),"Вы находитесь в разделе настройки интервалов тренировок").replyMarkup(keyboard));

            return "31";

        });

        SimpleScenarioStage<String, StageParams> st31 = new SimpleScenarioStage<>("31", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            if(mes.equals(msg_settings_training_time_show) || mes.equals(msg_settings_training_time_show_cmd)){
                Set<TrainingIntervals> intervals = botUserService.getUserTrainingIntervals(chat);
                int count = 1;
                for (TrainingIntervals interval: intervals) {
                    bot.execute(new SendMessage(chat.id(), "№" + (count++) + " - " + interval.getStartHour() + " ч."));
                }
                return "30";
            }else if(mes.equals(msg_settings_training_time_add) || mes.equals(msg_settings_training_time_add_cmd)){
                Keyboard keyboard = new ReplyKeyboardMarkup(
                        new KeyboardButton(msg_settings_back)
                )
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
                bot.execute(new SendMessage(chat.id(), "Введите час начала интервала (число от 1 до 24)").replyMarkup(keyboard));
                return "32";
            }else if(mes.equals(msg_settings_training_time_del) || mes.equals(msg_settings_training_time_del_cmd)){
                Keyboard keyboard = new ReplyKeyboardMarkup(
                        new KeyboardButton(msg_settings_back)
                )
                        .oneTimeKeyboard(true)   // optional
                        .resizeKeyboard(true)    // optional
                        .selective(true);        // optional
                SendMessage sm = new SendMessage(chat.id(), "Выберите интервал для удаления").parseMode(ParseMode.HTML);
                Set<TrainingIntervals> intervals = botUserService.getUserTrainingIntervals(chat);
                int count = 1;
                for (TrainingIntervals interval: intervals) {
                    InlineKeyboardButton kbDelServ = new  InlineKeyboardButton("№" + count++ + " - " + interval.getStartHour() + " ч.").callbackData("del#" + interval.getStartHour());
                    sm.replyMarkup(new InlineKeyboardMarkup(kbDelServ));
                }
                bot.execute(sm);
                return "33";
            }else if(mes.equals(msg_settings_training_time_clr) || mes.equals(msg_settings_training_time_clr_cmd)){
                return "30";
            }else if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                goToStage("1");
                doWork(p);
                return "1";
            }else if(mes.equals(msg_help) || mes.equals(msg_help_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы находитесь в разделе настроекинтервалов тнировок"));
                bot.execute(new SendMessage(chat.id(), "Здесь вы можете посмотреть настроенные интервалы, добавить новые, удалить выбранный интервал или все сразу."));
                bot.execute(new SendMessage(chat.id(), "Если заданы интервалы тренировок, бот будет предлагать начать тренировку в эти временные интервалы. Если нет заданных интервалов, бот не будет предлагать тренировку."));
                return "30";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню."));
                goToStage("30");
                doWork(p);
                return "30";
            }

        });

        SimpleScenarioStage<String, StageParams> st32 = new SimpleScenarioStage<>("32", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                goToStage("30");
                doWork(p);
                return "30";
            }else if(!mes.isEmpty()){
                mes=mes.trim();
                int hour = 0;
                if(mes.length()<=2){
                    try {
                        hour = Integer.valueOf(mes);
                    }catch (Exception e){
                        bot.execute(new SendMessage(chat.id(), "Введите целое число от 1 до 24"));
                    }
                }
                Set<TrainingIntervals> intervals = botUserService.getUserTrainingIntervals(chat);
                int count = 1;
                for (TrainingIntervals interval: intervals) {
                    bot.execute(new SendMessage(chat.id(), "№" + (count++) + " - " + interval.getStartHour() + " ч."));
                }
                return "30";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Введите час начала нового интервала (число от 1 до 24), или вернитесь в меню настроек интервалов."));
                return "32";
            }

        });

        SimpleScenarioStage<String, StageParams> st33 = new SimpleScenarioStage<>("33", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Введите час начала нового интервала (число от 1 до 24), или вернитесь в меню настроек интервалов."));
            return "32";

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
