package bot.engTrainer.botScenarios;

import bot.engTrainer.scenariodefine.simplescenario.SimpleScenarioStage;
import bot.engTrainer.services.BotService;
import bot.engTrainer.services.BotUserService;
import bot.engTrainer.services.ScenarioService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewUserConnectedScenario extends CommonScenario {

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
            goToStage("2");
            doWork(p);
            return "3";
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
                log.error(e.toString());
                bot.execute(new SendMessage(chat.id(), "Призошла ошибка. Повторите ввод, или напишите разработчикам polyashofff@yandex.ru"));
                return "3";
            }
        });

        addStage(st1);
        addStage(st2);
        addStage(st3);

    }

}
