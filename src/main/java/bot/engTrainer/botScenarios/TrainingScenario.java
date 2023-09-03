package bot.engTrainer.botScenarios;

import bot.engTrainer.entities.Dictionaries;
import bot.engTrainer.helpers.NextWord;
import bot.engTrainer.helpers.TrainingModes;
import bot.engTrainer.helpers.TrainingSummary;
import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
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

    public static final String mes_remember_new_words = "Запомните новые слова:";
    public static final String mes_exam_words = "Проверка ранее изученного:";

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
                    traininigService.isNextNewWord();
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

            traininigService.prepareNewTraining(chat, TrainingModes.TRAINING);

            showTrainingMenu(bot, chat);

            if(traininigService.isNextNewWord()){
                bot.execute(new SendMessage(chat.id(), mes_remember_new_words));
                goToStage("40");
                doWork(p);
                return "41";
            } else {
                bot.execute(new SendMessage(chat.id(), mes_exam_words));
                showWordWithVariants(bot, chat);
                goToStage("50");
                doWork(p);
                return "51";
            }

        });

        SimpleScenarioStage<String, StageParams> st40 = new SimpleScenarioStage<>("40", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();

            showTrainingMenu(bot, chat);

            if(traininigService.isNextNewWord()){
                showNewWord(bot, chat);
                return "41";
            }else if(traininigService.isNextExamWord()){
                bot.execute(new SendMessage(chat.id(), mes_exam_words));
                goToStage("50");
                doWork(p);
                return "51";
            } else {
                showTrainingMenu(bot, chat);
                bot.execute(new SendMessage(chat.id(), "Тренировка закончена"));
                bot.execute(new SendMessage(chat.id(), "Итоги тренировки:"));
                TrainingSummary ts = traininigService.getSummary();
                if(ts.getNewWords()>0) {
                    bot.execute(new SendMessage(chat.id(), "Выучено новых слов - " + ts.getNewWords()));
                }
                if(ts.getStudyWords()>0) {
                    bot.execute(new SendMessage(chat.id(), "Пвторено слов - " + ts.getStudyWords()));
                }
                return null;
            }

        });

        SimpleScenarioStage<String, StageParams> st41 = new SimpleScenarioStage<>("41", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            switch (mes) {
                case msg_training_next:
                case msg_training_next_cmd:
                    traininigService.goToNextWord(chat);
                    goToStage("40");
                    doWork(p);
                    return getCurrentStage().getIdentifier();
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

            showTrainingMenu(bot, chat);
            showWordWithVariants(bot, chat);
            return "51";

        });

        SimpleScenarioStage<String, StageParams> st51 = new SimpleScenarioStage<>("51", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();

            if(p.getMessage()!=null){
                String mes = p.getMessage().text();
                switch (mes) {
                    case msg_training_next:
                    case msg_training_next_cmd:
                        traininigService.goToNextWord(chat);
                        goToStage("40");
                        doWork(p);
                        return getCurrentStage().getIdentifier();
                    case msg_training_stop:
                    case msg_training_stop_cmd:
                        return null;
                    case msg_help:
                    case msg_help_cmd:
                        bot.execute(new SendMessage(chat.id(), "Вы находитесь в режиме треннировки"));
                        bot.execute(new SendMessage(chat.id(), "Сейчас вам показыватся слова пройденный ранее слова для пвторения."));
                        bot.execute(new SendMessage(chat.id(), "Тренировка будет закончена пока вы не дадите правильный ответ по каждому слову."));
                        bot.execute(new SendMessage(chat.id(), "Вам нужно выбирать правильные варианты перевода. Если выбрать вариант затруднительно, то можно перейти к следующему слову нажав Next, позде это слово появится снова"));
                        bot.execute(new SendMessage(chat.id(), "Для остановки и взврата в главное меню нажмите кнопку Stop"));
                        return "51";
                    default:
                        bot.execute(new SendMessage(chat.id(), "Я не понимаю"));
                        bot.execute(new SendMessage(chat.id(), "Выберите вариант перевода, нажмите Next для перехода к следующему слову, или Stop для возврата в главное меню."));
                        goToStage("40");
                        doWork(p);
                        return "51";
                }
            }

            String[] mesParts = p.getRequest().getText().split("#");

            if(mesParts[0].equals("variant")){
                String label = mesParts[1];
                if(label.equals("right")){
                    bot.execute(new SendMessage(chat.id(), "Правильный ответ :)").parseMode(ParseMode.HTML));
                    traininigService.acceptCorrectAnswer(chat);
                }else{
                    bot.execute(new SendMessage(chat.id(), "Неправильно :(").parseMode(ParseMode.HTML));
                    traininigService.acceptWrongAnswer(chat);
                }
                goToStage("40");
                doWork(p);
                return getCurrentStage().getIdentifier();
            }else{
                goToStage("40");
                doWork(p);
                return getCurrentStage().getIdentifier();
            }

        });



        addStage(st1);
        addStage(st2);
        addStage(st30);
        addStage(st40);
        addStage(st41);
        addStage(st50);
        addStage(st51);

    }

    private void showWordWithVariants(TelegramBot bot, Chat chat){

        NextWord nw = traininigService.getNextExamWord(chat);
        if(nw == null){
            return;
        }

    }

    private void showNewWord(TelegramBot bot, Chat chat){

        NextWord nw = traininigService.getNextNewWord(chat);
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
