package bot.engTrainer.botScenarios;

import bot.engTrainer.scenariodefine.Scenario;
import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;


public class MainMenuScenario extends CommonScenario {

    static final String msg_settings = "Settings";
    static final String msg_settings_cmd = "/settings";
    static final String msg_help = "Help";
    static final String msg_help_cmd = "/help";
    static final String msg_select_dictionary = "Select dictionary";
    static final String msg_select_dictionary_cmd = "/dictionary";
    static final String msg_start_training = "Start training";
    static final String msg_start_training_cmd = "/training";

    public MainMenuScenario() {
        super();
        setScenarioId("MainMenuScenario");
    }

    private void showMainMenu(TelegramBot bot, Chat chat){
        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(msg_start_training),
                new KeyboardButton(msg_select_dictionary),
                new KeyboardButton(msg_settings),
                new KeyboardButton(msg_help))
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
        bot.execute(new SendMessage(chat.id(),"Вы находитесь в главном меню").replyMarkup(keyboard));
    }

    @Override
    public void init() {

        SimpleScenarioStage<String, StageParams> st1 = new SimpleScenarioStage<>("1", (p) -> {
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            showMainMenu(bot, chat);
            return "2";
        });

        SimpleScenarioStage<String, StageParams> st2 = new SimpleScenarioStage<>("2", (p) -> {
            //if something wrong, return here
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            if(mes.equals(msg_settings) || mes.equals(msg_settings_cmd)){
                Scenario<String, StageParams> sc = botService.startScenario("SettingsScenario", chat);
                sc.doWork(p);
                return null;
            }else if(mes.equals(msg_select_dictionary) || mes.equals(msg_select_dictionary_cmd)){
                Scenario<String, StageParams> sc = botService.startScenario("MyDictionarySetupScenario", chat);
                sc.doWork(p);
                return null;
            }else if(mes.equals(msg_start_training) || mes.equals(msg_start_training_cmd)){
                Scenario<String, StageParams> sc = botService.startScenario("TrainingScenario", chat);
                sc.doWork(p);
                return null;
            }else if(mes.equals(msg_help) || mes.equals(msg_help_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы находитесь в главном меню"));
                bot.execute(new SendMessage(chat.id(), "Вы можете выполнить настройки своего профиля, подключить словари для тренировки, или начать трнировку прямо сейчас."));
                bot.execute(new SendMessage(chat.id(), "Выберите пункт меню, в каждом из них есть своя справка."));
                return "2";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню и действуюте по инструкциям."));
                goToStage("1");
                doWork(p);
                return "2";
            }

        });

        SimpleScenarioStage<String, StageParams> st7 = new SimpleScenarioStage<>("7", (p) -> {
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();
            showMainMenu(bot, chat);
            return "2";
        });

        addStage(st1);
        addStage(st2);
        addStage(st7);

    }

    @Override
    public void resume(Object param) {
        TelegramBot bot = new TelegramBot(botService.getBotConfig().getToken());
        goToStage("2");
        showMainMenu(bot, botService.getCurrentChat());
    }


}
