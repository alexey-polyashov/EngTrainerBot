package bot.engTrainer.botScenarios;

import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
import bot.engTrainer.services.BotService;
import bot.engTrainer.services.ScenarioService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;



public class TrainingScenario extends CommonScenario {

    static final String msg_help = "Help";
    static final String msg_help_cmd = "/help";
    static final String msg_mainmenu = "Later";
    static final String msg_mainmenu_cmd = "/later";
    static final String msg_training_ready = "Start";
    static final String msg_training_ready_cmd = "/start";

    public TrainingScenario() {
        super();
        setScenarioId("TrainingScenario");
    }

    private void showMainMenu(TelegramBot bot, Chat chat){
        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(msg_training_ready),
                new KeyboardButton(msg_help),
                new KeyboardButton(msg_mainmenu))
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
        bot.execute(new SendMessage(chat.id(),"Вы находитесь "));
        bot.execute(new SendMessage(chat.id(),"Тренировка займет некоторое время."));
        bot.execute(new SendMessage(chat.id(),"Для начала тренировки нужно выбрать '" + msg_training_ready + "'").replyMarkup(keyboard));
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
    public void resume(Object param) {
        TelegramBot bot = new TelegramBot(botService.getBotConfig().getToken());
        showMainMenu(bot, botService.getCurrentChat());
    }


}
