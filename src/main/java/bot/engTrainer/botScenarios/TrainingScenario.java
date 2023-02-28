package bot.engTrainer.botScenarios;

import bot.engTrainer.helpers.NextWord;
import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
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
    static final String msg_training_next = "Next";
    static final String msg_training_next_cmd = "/next";
    static final String msg_training_stop = "Stop";
    static final String msg_training_stop_cmd = "/stop";

    public TrainingScenario() {
        super();
        setScenarioId("TrainingScenario");
    }

    private void showMainMenu(TelegramBot bot, Chat chat){
        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(msg_training_ready),
                new KeyboardButton(msg_help),
                new KeyboardButton(msg_mainmenu))
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
        bot.execute(new SendMessage(chat.id(),"Вы находитесь в разделе тренировок"));
        bot.execute(new SendMessage(chat.id(),"Тренировка займет некоторое время."));
        bot.execute(new SendMessage(chat.id(),"Для начала тренировки нужно выбрать '" + msg_training_ready + "'").replyMarkup(keyboard));
    }

    private void showTrainingMenu(TelegramBot bot, Chat chat){
        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(msg_training_next),
                new KeyboardButton(msg_help),
                new KeyboardButton(msg_training_stop))
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
        bot.execute(new SendMessage(chat.id(),"").replyMarkup(keyboard));
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
                    return null;
                case msg_help:
                case msg_help_cmd:
                    bot.execute(new SendMessage(chat.id(), "Вы находитесь в разделе тренировки"));
                    bot.execute(new SendMessage(chat.id(), "Вы можете начать тренировку, или отложить ее."));
                    bot.execute(new SendMessage(chat.id(), "Во время начала тренировки бот подберет слова из подключенных словарей, исходя из выбранной интенсивности тренировок и результатов предыдущих тренировок. "));
                    bot.execute(new SendMessage(chat.id(), "Старайтесь отвечать обдуманно. После тренировки, ваши ответы запишутся в прогресс обучения."));
                    return "2";
                default:
                    bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню."));
                    goToStage("1");
                    doWork(p);
                    return "2";
            }

        });

        SimpleScenarioStage<String, StageParams> st30 = new SimpleScenarioStage<>("30", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            traininigService.prepareNewTraining(chat);

            showTrainingMenu(bot, chat);


            if(traininigService.newWordsContains()){
                bot.execute(new SendMessage(chat.id(), "Запомните новые слова:"));
                goToStage("40");
                doWork(p);
                return "41";
            } else {
                bot.execute(new SendMessage(chat.id(), "Проверка ранее изученного:"));
                showWordWithVariants(bot, chat);
                goToStage("50");
                doWork(p);
                return "51";
            }

        });

        SimpleScenarioStage<String, StageParams> st40 = new SimpleScenarioStage<>("40", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            showTrainingMenu(bot, chat);

            if(traininigService.newWordsContains()){
                showNewWord(bot, chat);
                return "41";
            } else {
                bot.execute(new SendMessage(chat.id(), "Проверка ранее изученного:"));
                goToStage("50");
                doWork(p);
                return "51";
            }

        });

        SimpleScenarioStage<String, StageParams> st41 = new SimpleScenarioStage<>("41", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            switch (mes) {
                case msg_training_next:
                case msg_training_next_cmd:
                    traininigService.learnNewWord(chat);
                    goToStage("40");
                    doWork(p);
                    return "41";
                case msg_training_stop:
                case msg_training_stop_cmd:
                    return null;
                case msg_help:
                case msg_help_cmd:
                    bot.execute(new SendMessage(chat.id(), "Вы находитесь в режиме треннировки"));
                    bot.execute(new SendMessage(chat.id(), "Сейчас вам показыватся новые слова для изучения из выбранных вами словарей."));
                    bot.execute(new SendMessage(chat.id(), "Вам нужно запомнить их. Для перехода к следующему слову нажмите кнопку Next"));
                    bot.execute(new SendMessage(chat.id(), "Для остановки и взврата в главное меню нажмите кнопку Stop"));
                    return "41";
                default:
                    bot.execute(new SendMessage(chat.id(), "Нажмите Next для перехода к следующему слову, или Stop для возврата в главное меню."));
                    return "41";
            }

        });

        SimpleScenarioStage<String, StageParams> st50 = new SimpleScenarioStage<>("50", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            showTrainingMenu(bot, chat);
            showWordWithVariants(bot, chat);
            return "51";

        });


        addStage(st1);
        addStage(st2);
        addStage(st30);
        addStage(st40);
        addStage(st41);
        addStage(st50);

    }

    private void showWordWithVariants(TelegramBot bot, Chat chat){

        NextWord nw = traininigService.getNextWord(chat);
        if(nw == null){
            return;
        }

    }

    private void showNewWord(TelegramBot bot, Chat chat){

        NextWord nw = traininigService.getNextWord(chat);
        if(nw == null){
            return;
        }
        bot.execute(new SendMessage(chat.id(), ""));

    }

    private void acceptAnswer(TelegramBot bot, Chat chat){


    }

    @Override
    public void resume(Object param) {
        TelegramBot bot = new TelegramBot(botService.getBotConfig().getToken());
        showMainMenu(bot, botService.getCurrentChat());
    }


}
