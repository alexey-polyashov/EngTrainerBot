package bot.engTrainer.services;

import bot.engTrainer.botScenarios.CommonScenario;
import bot.engTrainer.botScenarios.SimpleScManagerConfig;
import bot.engTrainer.botScenarios.StageParams;
import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.UserStack;
import bot.engTrainer.exceptions.SWUException;
import bot.engTrainer.exceptions.ScenarioMissing;
import bot.engTrainer.repository.UserStackRepository;
import bot.engTrainer.scenariodefine.Scenario;
import com.github.kshashov.telegram.api.TelegramRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
@RequiredArgsConstructor
@Slf4j
@Scope("prototype")
public class BotService {

    private final SimpleScManagerConfig simpleScManager;
    private final BotUserService botUserService;
    private final ScenarioService scenarioService;
    private final UserStackRepository userStackRepository;

    final String msg_settings = "Settings";
    final String msg_select_dictionary = "Select dictionary";
    final String msg_start_training = "Start training";

    private Chat currentChat;

    public Chat getCurrentChat() {
        return currentChat;
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }

    Stack<Scenario<String, StageParams>> scenarioStack = new Stack<>();

    public Scenario<String, StageParams> startScenario(String scId, Chat chat) {
        log.info("Get scenario for id - {}, in chat:{}", scId, chat.id());
        CommonScenario<String, StageParams> sc = (CommonScenario<String, StageParams>)simpleScManager.getScenarioById(scId)
                .orElseThrow(()->new ScenarioMissing(String.format("chat:%1$S, Не найден сценарий по id - %2$S", chat.id(), scId)));
        sc.setBotService(this);
        sc.setScenarioService(scenarioService);
        scenarioStack.push(sc);
        sc.start();
        return sc;
    }

    public Optional<BotUser> getUser(String userId, Chat chat){
        return botUserService.getUser(userId, chat);
    }

    public void loadChat() {
        scenarioStack.clear();
        List<UserStack> loadList = userStackRepository.findByChatIdOrderByNumber(currentChat.id());
        Iterator<UserStack> it = loadList.listIterator();
        while(it.hasNext()){
            UserStack el = it.next();
            Scenario<String, StageParams> newScen = simpleScManager.getScenarioById(el.getScenarioId())
                    .orElseThrow(()->new SWUException("Ошибка восстановления сеанса, сценарий '" + el.getScenarioId() + "' не найден"));
            CommonScenario<String, StageParams> comScen  = (CommonScenario<String, StageParams> )newScen;
            comScen.setScenarioService(scenarioService);
            comScen.setBotService(this);
            comScen.load(currentChat.id());
            scenarioStack.push(comScen);
        }
    }

    @Transactional
    public void saveChat() {
        int order = 1;
        Iterator<Scenario<String, StageParams>> it = scenarioStack.iterator();
        scenarioService.deleteByChatId(currentChat.id());
        userStackRepository.deleteByChatId(currentChat.id());
        while(it.hasNext()){
            Scenario<String, StageParams> scen = it.next();
            scen.save();
            UserStack userStack = UserStack.builder()
                    .number(order++)
                    .chatId(currentChat.id())
                    .scenarioId(scen.getId())
                    .build();
            userStackRepository.save(userStack);
        }
    }

    private void checkScenStack(){
        if (!scenarioStack.empty()){
            Scenario<String, StageParams> sc = scenarioStack.peek();
            if(sc.getCurrentStage()==null){
                endCurrentScenario();
            }
        }
    }

    public void doWork(String mes, TelegramBot bot, Chat chat, Message fullMes, TelegramRequest request, String opt) throws Throwable {

        if(scenarioStack.empty()) {
            Optional<BotUser> botUser = botUserService.getUserByChat(chat);
            if (botUser.isEmpty()) {
                Scenario<String, StageParams> sc = startScenario("NewUserConnectedScenario", currentChat);
                StageParams p = StageParams.builder().bot(bot).chat(chat).message(fullMes).request(request).build();
                sc.doWork(p);
                checkScenStack();
                return;
            }else if(opt.equals("cmd")){
                if(fullMes.text().equals("/myservices")){
                    Scenario<String, StageParams> sc = startScenario("ServiceSelectScenario", currentChat);
                    StageParams p = StageParams.builder().bot(bot).chat(chat).message(fullMes).request(request).build();
                    sc.doWork(p);
                    checkScenStack();
                    return;
                }
            }
            else{
                bot.execute(new SendMessage(chat.id(), "Здравствуйте!"));
                bot.execute(new SendMessage(chat.id(), "Я бот компании Спецобъединение \"Северо-Запад\""));
                bot.execute(new SendMessage(chat.id(), "Я пока ничего не умею, но скоро меня научат"));
            }
        }else{
            Scenario<String, StageParams> sc = scenarioStack.peek();
            StageParams p = StageParams.builder().bot(bot).chat(chat).message(fullMes).request(request).build();
            sc.doWork(p);
            checkScenStack();
            return;
        }

    }

    public void endCurrentScenario() {
        if (!scenarioStack.empty()){
            scenarioStack.pop();
        }
    }

    public Long saveBotUser(BotUser botUser) {
        botUserService.saveBotUser(botUser);
        return null;
    }

}
