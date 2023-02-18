package bot.engTrainer.botScenarios;

import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.Dictionaries;
import bot.engTrainer.entities.TrainingIntervals;
import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
import bot.engTrainer.services.DictionaryService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.*;


public class SettingsScenario extends CommonScenario {

    private DictionaryService dictionaryService;

    static final String msg_settings_training_time = "Training intervals";
    static final String msg_settings_training_time_cmd = "/intervals";
    static final String msg_settings_intensive = "Intensity";
    static final String msg_settings_intensive_cmd = "/intensity";
    static final String msg_settings_dictionaries = "Dictionaries";
    static final String msg_settings_dictionaries_cmd = "/dictionaries";
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
    static final String msg_settings_dictionaries_show = "Selected dictionaries";
    static final String msg_settings_dictionaries_show_cmd = "/show";
    static final String msg_settings_dictionaries_add = "Plug dictionary";
    static final String msg_settings_dictionaries_add_cmd = "/plug";
    static final String msg_settings_dictionaries_del = "Unplug dictionary";
    static final String msg_settings_dictionaries_del_cmd = "/unplug";

    public SettingsScenario() {
        super();
        setScenarioId("SettingsScenario");
    }

    private void showMainMenu(TelegramBot bot, Chat chat){
        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(msg_settings_training_time),
                new KeyboardButton(msg_settings_intensive),
                new KeyboardButton(msg_settings_dictionaries),
                new KeyboardButton(msg_help),
                new KeyboardButton(msg_settings_mainmenu))
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
        bot.execute(new SendMessage(chat.id(),"Вы находитесь в разделе настроек").replyMarkup(keyboard));
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

            if(mes.equals(msg_settings_training_time) || mes.equals(msg_settings_training_time_cmd)){
                goToStage("30");
                doWork(p);
                return "31";
            }else if(mes.equals(msg_settings_dictionaries) || mes.equals(msg_settings_dictionaries_cmd)){
                goToStage("40");
                doWork(p);
                return "41";
            }else if(mes.equals(msg_settings_intensive) || mes.equals(msg_settings_intensive_cmd)){
                goToStage("50");
                doWork(p);
                return "51";
            }else if(mes.equals(msg_settings_mainmenu) || mes.equals(msg_settings_mainmenu_cmd)){
                return null;
            }else if(mes.equals(msg_help) || mes.equals(msg_help_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы находитесь в разделе настроек"));
                bot.execute(new SendMessage(chat.id(), "Здесь вы можете настроить временные интервалы тренировок. Бот запустит тренировку автоматически в выбранные интервалы времени. "));
                bot.execute(new SendMessage(chat.id(), "Можете настроить интенсивность - количество слов за одну тренировку"));
                return "2";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню."));
                goToStage("1");
                doWork(p);
                return "2";
            }

        });


        SimpleScenarioStage<String, StageParams> st30 = new SimpleScenarioStage<>("30", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();

            Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(msg_settings_training_time_show),
                new KeyboardButton(msg_settings_training_time_add),
                new KeyboardButton(msg_settings_training_time_del),
                new KeyboardButton(msg_settings_training_time_clr),
                new KeyboardButton(msg_help),
                new KeyboardButton(msg_settings_back)
            )
            .oneTimeKeyboard(false)   // optional
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
                if(intervals.size()==0){
                    bot.execute(new SendMessage(chat.id(), "Интервалы тренировок не заданы"));
                }else {
                    for (TrainingIntervals interval : intervals) {
                        bot.execute(new SendMessage(chat.id(), "№" + (count++) + " - " + interval.getStartHour() + " ч."));
                    }
                }
                return "31";
            }else if(mes.equals(msg_settings_training_time_add) || mes.equals(msg_settings_training_time_add_cmd)){
                Keyboard keyboard = new ReplyKeyboardMarkup(
                        new KeyboardButton(msg_settings_back)
                )
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
                bot.execute(new SendMessage(chat.id(), "Введите час начала интервала (число от 1 до 24)").replyMarkup(keyboard));
                return "32";
            }else if(mes.equals(msg_settings_training_time_del) || mes.equals(msg_settings_training_time_del_cmd)){
                Keyboard keyboard = new ReplyKeyboardMarkup(
                        new KeyboardButton(msg_settings_back)
                )
                        .oneTimeKeyboard(false)   // optional
                        .resizeKeyboard(true)    // optional
                        .selective(true);        // optional
                SendMessage sm = new SendMessage(chat.id(), "Выберите интервал для удаления").parseMode(ParseMode.HTML);
                Set<TrainingIntervals> intervals = botUserService.getUserTrainingIntervals(chat);
                int count = 1;
                InlineKeyboardMarkup kbd = new InlineKeyboardMarkup();
                for (TrainingIntervals interval: intervals) {
                    InlineKeyboardButton kbDelServ = new  InlineKeyboardButton("№" + count++ + " - " + interval.getStartHour() + " ч.").callbackData("del#" + interval.getStartHour());
                    kbd.addRow(kbDelServ);
                }
                sm.replyMarkup(kbd);
                bot.execute(sm);
                return "33";
            }else if(mes.equals(msg_settings_training_time_clr) || mes.equals(msg_settings_training_time_clr_cmd)){
                Keyboard keyboard = new ReplyKeyboardMarkup(
                        new KeyboardButton(msg_settings_back)
                )
                        .oneTimeKeyboard(false)   // optional
                        .resizeKeyboard(true)    // optional
                        .selective(true);        // optional
                SendMessage sm = new SendMessage(chat.id(), "Подтвердите очистку интервалов тренировки. Нажмите YES").parseMode(ParseMode.HTML);
                Set<TrainingIntervals> intervals = botUserService.getUserTrainingIntervals(chat);
                int count = 1;
                for (TrainingIntervals interval: intervals) {
                    InlineKeyboardButton kbDelServ = new  InlineKeyboardButton("YES").callbackData("clr#yes");
                    sm.replyMarkup(new InlineKeyboardMarkup(kbDelServ));
                }
                bot.execute(sm);
                return "34";
            }else if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                goToStage("1");
                doWork(p);
                return "2";
            }else if(mes.equals(msg_help) || mes.equals(msg_help_cmd)){
                bot.execute(new SendMessage(chat.id(), "Вы находитесь в разделе настроекинтервалов тнировок"));
                bot.execute(new SendMessage(chat.id(), "Здесь вы можете посмотреть настроенные интервалы, добавить новые, удалить выбранный интервал или все сразу."));
                bot.execute(new SendMessage(chat.id(), "Если заданы интервалы тренировок, бот будет предлагать начать тренировку в эти временные интервалы. Если нет заданных интервалов, бот не будет предлагать тренировку."));
                return "31";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню."));
                goToStage("30");
                doWork(p);
                return "31";
            }

        });

        SimpleScenarioStage<String, StageParams> st32 = new SimpleScenarioStage<>("32", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                goToStage("30");
                doWork(p);
                return "31";
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
                botUserService.addUserTrainingInterval(chat, hour);
                bot.execute(new SendMessage(chat.id(), "Тренировка на " + hour + "ч. добавлена"));
                goToStage("30");
                doWork(p);
                return "31";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Введите час начала нового интервала (число от 1 до 24), или вернитесь в меню настроек интервалов."));
                return "32";
            }

        });

        SimpleScenarioStage<String, StageParams> st33 = new SimpleScenarioStage<>("33", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();

            if(p.getMessage()!=null){
                String mes = p.getMessage().text();
                if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                    goToStage("30");
                    doWork(p);
                    return "31";
                }
            }

            String[] mesParts = p.getRequest().getText().split("#");
            switch (mesParts[0]) {
                case "del":
                    String sHour = mesParts[1];
                    int iHour = Integer.parseInt(sHour);
                    if(iHour>0 && iHour<=24){
                        botUserService.delUserTrainingIntervals(chat, iHour);
                        bot.execute(new SendMessage(chat.id(), "Интервал тренировки на " + iHour + "ч. удален"));
                        goToStage("30");
                        doWork(p);
                        return "31";
                    }
                default:
                    bot.execute(new SendMessage(chat.id(), "Извините, я вас не понял"));
                    bot.execute(new SendMessage(chat.id(), "Выберите интервал для удаления, или вернитесь назад"));
                    return "33";
            }
        });

        SimpleScenarioStage<String, StageParams> st34 = new SimpleScenarioStage<>("34", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();

            if(p.getMessage()!=null){
                String mes = p.getMessage().text();
                if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                    goToStage("30");
                    doWork(p);
                    return "31";
                }
            }

            String[] mesParts = p.getRequest().getText().split("#");
            switch (mesParts[0]) {
                case "clr":
                    if(mesParts[1].equals("yes")){
                        botUserService.clearUserTrainingIntervals(chat);
                        bot.execute(new SendMessage(chat.id(), "Интервалы тренировок удалены"));
                        goToStage("30");
                        doWork(p);
                        return "31";
                    }
                default:
                    bot.execute(new SendMessage(chat.id(), "Извините, я вас не понял"));
                    bot.execute(new SendMessage(chat.id(), "Нажмите 'YES' для очистки интервалов тренировок, или вернитесь назад"));
                    return "34";
            }
        });

        SimpleScenarioStage<String, StageParams> st40 = new SimpleScenarioStage<>("40", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            Keyboard keyboard = new ReplyKeyboardMarkup(
                    new KeyboardButton(msg_settings_dictionaries_show),
                    new KeyboardButton(msg_settings_dictionaries_add),
                    new KeyboardButton(msg_settings_dictionaries_del),
                    new KeyboardButton(msg_help),
                    new KeyboardButton(msg_settings_back)
            )
                    .oneTimeKeyboard(false)   // optional
                    .resizeKeyboard(true)    // optional
                    .selective(true);        // optional
            bot.execute(new SendMessage(chat.id(),"Вы находитесь в разделе настройки интервалов словарей").replyMarkup(keyboard));

            return "41";

        });

        SimpleScenarioStage<String, StageParams> st41 = new SimpleScenarioStage<>("41", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            switch (mes) {
                case msg_settings_dictionaries_show:
                case msg_settings_dictionaries_show_cmd: {
                    Set<Dictionaries> dicts = botUserService.getSelectedDictionaries(chat);
                    int count = 1;
                    for (Dictionaries dict : dicts) {
                        Keyboard keyboard = new ReplyKeyboardMarkup(
                                new KeyboardButton(msg_settings_back)
                        );
                        bot.execute(new SendMessage(chat.id(), "<b>№" + (count++) + " - " + dict.getName() + "</b>\n" +
                                "<u>" + dict.getDescription() + "</u>").parseMode(ParseMode.HTML));
                    }
                    return "40";
                }
                case msg_settings_dictionaries_add:
                case msg_settings_dictionaries_add_cmd: {
                    List<Dictionaries> dicts = dictionaryService.getDictionaries();
                    int count = 1;
                    bot.execute(new SendMessage(chat.id(), "Нажмите 'Добавить' под выбранным словарем"));
                    for (Dictionaries dict : dicts) {
                        InlineKeyboardButton keyLine = new InlineKeyboardButton("Добавить - " + dict.getName()).callbackData("plug#" + dict.getId());
                        bot.execute(new SendMessage(chat.id(), "<b>№" + (count++) + " - " + dict.getName() + "</b>\n" +
                                "<u>" + dict.getDescription() + "</u>").parseMode(ParseMode.HTML).replyMarkup(new InlineKeyboardMarkup(keyLine)));
                    }
                    Keyboard keyboard = new ReplyKeyboardMarkup(
                            new KeyboardButton(msg_settings_back)
                    )
                            .oneTimeKeyboard(false)   // optional
                            .resizeKeyboard(true)    // optional
                            .selective(true);        // optional

                    return "42";
                }
                case msg_settings_training_time_del:
                case msg_settings_training_time_del_cmd: {
                    Set<Dictionaries> dicts = botUserService.getSelectedDictionaries(chat);
                    int count = 1;
                    bot.execute(new SendMessage(chat.id(), "Нажмите 'Отключить' под выбранным словарем"));
                    for (Dictionaries dict : dicts) {
                        InlineKeyboardButton keyLine = new InlineKeyboardButton("Отключить - " + dict.getName()).callbackData("unplug#" + dict.getId());
                        bot.execute(new SendMessage(chat.id(), "<b>№" + (count++) + " - " + dict.getName() + "</b>\n" +
                                "<u>" + dict.getDescription() + "</u>").parseMode(ParseMode.HTML).replyMarkup(new InlineKeyboardMarkup(keyLine)));
                    }
                    Keyboard keyboard = new ReplyKeyboardMarkup(
                            new KeyboardButton(msg_settings_back)
                    )
                            .oneTimeKeyboard(false)   // optional
                            .resizeKeyboard(true)    // optional
                            .selective(true);        // optional

                    return "43";
                }
                case msg_settings_back:
                case msg_settings_back_cmd:
                    goToStage("1");
                    doWork(p);
                    return "2";
                case msg_help:
                case msg_help_cmd:
                    bot.execute(new SendMessage(chat.id(), "Вы находитесь в разделе выбора словарей"));
                    bot.execute(new SendMessage(chat.id(), "Здесь вы можете посмотреть выбранные словари для изучения, подключить или отключить словаь."));
                    bot.execute(new SendMessage(chat.id(), "Из выбранных словарей, бот будет предлагать слова во время тренировок. Если нет выбранных словарей, бот будет испоьзовать все словари."));
                    return "40";
                default:
                    bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите пункт меню."));
                    goToStage("40");
                    doWork(p);
                    return "41";
            }

        });

        SimpleScenarioStage<String, StageParams> st42 = new SimpleScenarioStage<>("42", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();
            String[] mesParts = p.getMessage().text().split("#");

            if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                goToStage("40");
                doWork(p);
                return "41";
            }else if(mesParts[0].equals("plug")){
                long id = Long.parseLong(mesParts[1]);
                Dictionaries dict = dictionaryService.getById(id);
                botUserService.addDictionary(chat, dict);
                bot.execute(new SendMessage(chat.id(), "Словарь <b>'" + dict.getName() + "'</b> подключен"));
                return "40";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите словарь для подключения и нажмите кнопку под ним."));
                return "42";
            }

        });

        SimpleScenarioStage<String, StageParams> st43 = new SimpleScenarioStage<>("43", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();
            String[] mesParts = p.getMessage().text().split("#");

            if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                goToStage("40");
                doWork(p);
                return "41";
            }else if(mesParts[0].equals("unplug")){
                long id = Long.parseLong(mesParts[1]);
                Dictionaries dict = dictionaryService.getById(id);
                botUserService.delDictionary(chat, dict);
                bot.execute(new SendMessage(chat.id(), "Словарь <b>'" + dict.getName() + "'</b> отключен"));
                return "40";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Выберите словарь для отключения и нажмите кнопку под ним."));
                return "43";
            }

        });




        SimpleScenarioStage<String, StageParams> st50 = new SimpleScenarioStage<>("50", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();
            Optional<BotUser> botUser = botUserService.getUserByChat(chat);
            bot.execute(new SendMessage(chat.id(), "Вы в разделе настройки интенсивности тренировок"));
            Keyboard keyboard = new ReplyKeyboardMarkup(
                    new KeyboardButton(msg_settings_back)
                )
                .oneTimeKeyboard(false)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
            if(botUser.isPresent()){
                bot.execute(new SendMessage(chat.id(),"Ваша текущая интенсивность тренировок - " + botUser.get().getTrainigIntensity() + " слов в день").replyMarkup(keyboard));
                bot.execute(new SendMessage(chat.id(),"Для изменения введите целое число в диапазоне от 1 до 50, равное количеству слов в день, которое вы хотите изучать. Для отмены изменений вернитесь назад.").replyMarkup(keyboard));
            }else{
                bot.execute(new SendMessage(chat.id(),"Ошибка! Вашего аккаунта нет в базе").replyMarkup(keyboard));
            }

            return "51";

        });

        SimpleScenarioStage<String, StageParams> st51 = new SimpleScenarioStage<>("51", (p) -> {

            Chat chat = p.getChat();
            TelegramBot bot = p.getBot();
            String mes = p.getMessage().text();

            if(mes.equals(msg_settings_back) || mes.equals(msg_settings_back_cmd)){
                goToStage("1");
                doWork(p);
                return "2";
            }else if(!mes.isEmpty()){
                mes=mes.trim();
                int intensity = 0;
                if(mes.length()<=2){
                    try {
                        intensity = Integer.parseInt(mes);
                    }catch (Exception e){
                        bot.execute(new SendMessage(chat.id(), "Введите целое число от 1 до 50"));
                        return "51";
                    }
                }
                Optional<BotUser> botUser = botUserService.getUserByChat(chat);
                if(botUser.isPresent()){
                    BotUser entity = botUser.get();
                    entity.setTrainigIntensity(intensity);
                    botUserService.saveBotUser(entity);
                    bot.execute(new SendMessage(chat.id(), "Установлена интенсивность тренировок - " + intensity));
                }
                goToStage("1");
                doWork(p);
                return "2";
            }else{
                bot.execute(new SendMessage(chat.id(), "Я вас не понимаю. Для изменения введите целое число в диапазоне от 1 до 50, равное количеству слов в день, которое вы хотите изучать. Для отмены изменений вернитесь назад."));
                return "51";
            }

        });

        addStage(st1);
        addStage(st2);
        addStage(st30);
        addStage(st31);
        addStage(st32);
        addStage(st33);
        addStage(st34);
        addStage(st40);
        addStage(st41);
        addStage(st42);
        addStage(st43);
        addStage(st50);
        addStage(st51);

    }

    @Override
    public void resume(Object param) {
        TelegramBot bot = new TelegramBot(botService.getBotConfig().getToken());
        goToStage("2");
        showMainMenu(bot, botService.getCurrentChat());
    }


}
