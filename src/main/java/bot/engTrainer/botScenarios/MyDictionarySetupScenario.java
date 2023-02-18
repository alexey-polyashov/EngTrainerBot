package bot.engTrainer.botScenarios;

import bot.engTrainer.exceptions.BotException;
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


public class MyDictionarySetupScenario extends CommonScenario {

    static final String msg_mydictionary_show_words = "Show words";
    static final String msg_mydictionary_show_words_cmd = "/showwords";
    static final String msg_mydictionary_add_word = "Add word";
    static final String msg_mydictionary_add_word_cmd = "/add";
    static final String msg_mydictionary_del_word = "Delete word";
    static final String msg_mydictionary_del_word_cmd = "/del";
    static final String msg_mydictionary_clear = "Clear";
    static final String msg_mydictionary_clear_cmd = "/clear";
    static final String msg_help = "Help";
    static final String msg_help_cmd = "/help";
    static final String msg_mainmenu = "Main menu";
    static final String msg_mainmenu_cmd = "/mainmenu";
    static final String msg_settings_back = "Back";
    static final String msg_settings_back_cmd = "/back";

    public MyDictionarySetupScenario() {
        super();
        setScenarioId("MyDictionarySetupScenario");
    }

    private void showMainMenu(TelegramBot bot, Chat chat){
        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(msg_mydictionary_show_words),
                new KeyboardButton(msg_mydictionary_add_word),
                new KeyboardButton(msg_mydictionary_del_word),
                new KeyboardButton(msg_mydictionary_clear),
                new KeyboardButton(msg_help),
                new KeyboardButton(msg_mainmenu))
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
        bot.execute(new SendMessage(chat.id(),"Вы находитесь в разделе настроек своего словаря").replyMarkup(keyboard));
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
            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            bot.execute(new SendMessage(chat.id(), "Словарь настроен."));
            return null;
        });

        addStage(st1);
        addStage(st2);

    }

    @Override
    public void resume(Object param) {
        TelegramBot bot = new TelegramBot(botService.getBotConfig().getToken());
        showMainMenu(bot, botService.getCurrentChat());
    }

}
