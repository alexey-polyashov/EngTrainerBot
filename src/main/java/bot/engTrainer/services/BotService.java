package bot.engTrainer.services;

import bot.engTrainer.botScenarios.CommonScenario;
import bot.engTrainer.botScenarios.SimpleScManagerConfig;
import bot.engTrainer.botScenarios.StageParams;
import bot.engTrainer.config.BotConfig;
import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.UserStack;
import bot.engTrainer.exceptions.BotException;
import bot.engTrainer.exceptions.ScenarioMissing;
import bot.engTrainer.repository.UserStackRepository;
import bot.engTrainer.scenariodefine.Scenario;
import com.github.kshashov.telegram.api.TelegramRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
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
    private final BotConfig botConfig;

    private Chat currentChat;

    public Chat getCurrentChat() {
        return currentChat;
    }

    public BotConfig getBotConfig() {
        return botConfig;
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }

    Stack<CommonScenario> scenarioStack = new Stack<>();

    public CommonScenario startScenario(String scId, Chat chat) {
        CommonScenario sc = (CommonScenario)simpleScManager.getScenarioById(scId)
                .orElseThrow(()->new ScenarioMissing(String.format("chat:%1$S, Не найден сценарий по id - %2$S", chat.id(), scId)));
        sc.setBotService(this);
        sc.setScenarioService(scenarioService);
        sc.setBotUserService(botUserService);
        scenarioStack.push(sc);
        sc.start();
        return sc;
    }

    public Optional<BotUser> getUser(String userId){
        return botUserService.getUser(userId);
    }

    public void loadChat() {
        scenarioStack.clear();
        List<UserStack> loadList = userStackRepository.findByChatIdOrderByNumber(currentChat.id());
        Iterator<UserStack> it = loadList.listIterator();
        while(it.hasNext()){
            UserStack el = it.next();
            Scenario<String, StageParams> newScen = simpleScManager.getScenarioById(el.getScenarioId())
                    .orElseThrow(()->new BotException("Ошибка восстановления сеанса, сценарий '" + el.getScenarioId() + "' не найден"));
            CommonScenario comScen  = (CommonScenario )newScen;
            comScen.setScenarioService(scenarioService);
            comScen.setBotUserService(botUserService);
            comScen.setBotService(this);
            comScen.load(currentChat.id());
            scenarioStack.push(comScen);
        }
    }

    @Transactional
    public void saveChat() {
        int order = 1;
        Iterator<CommonScenario> it = scenarioStack.iterator();
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

    public void checkScenStack(){
        if (!scenarioStack.empty()){
            Scenario<String, StageParams> sc = scenarioStack.peek();
            if(sc.getCurrentStage()==null){
                endCurrentScenario(currentChat);
            }
        }
    }

    public void doWork(String mes, TelegramBot bot, Chat chat, Message fullMes, TelegramRequest request, String opt) throws Throwable {
        if(scenarioStack.empty() || mes.equals("/start")) {
            Optional<BotUser> botUser = botUserService.getUserByChat(chat);
            scenarioStack.clear();
            if (botUser.isEmpty()) {
                CommonScenario sc = startScenario("NewUserConnectedScenario", currentChat);
                StageParams p = StageParams.builder().bot(bot).chat(chat).message(fullMes).request(request).build();
                sc.doWork(p);
                checkScenStack();
                return;
            }else{
                CommonScenario sc = startScenario("MainMenuScenario", currentChat);
                StageParams p = StageParams.builder().bot(bot).chat(chat).message(fullMes).request(request).build();
                sc.doWork(p);
                checkScenStack();
                return;
            }
        }else{
            CommonScenario sc = scenarioStack.peek();
            StageParams p = StageParams.builder().bot(bot).chat(chat).message(fullMes).request(request).build();
            sc.doWork(p);
            checkScenStack();
            return;
        }

    }

    public void endCurrentScenario(Chat chat) {
        if (!scenarioStack.empty()){
            scenarioStack.pop();
            if(scenarioStack.empty()){
                CommonScenario sc = startScenario("MainMenuScenario", currentChat);
                TelegramBot bot = new TelegramBot(botConfig.getToken());
                StageParams p = StageParams.builder().bot(bot).chat(chat).build();
                sc.doWork(p);
            }else {
                CommonScenario cs = scenarioStack.peek();
                cs.resume(chat);
            }
        }
    }

    public Long saveBotUser(BotUser botUser) {
        botUserService.saveBotUser(botUser);
        return null;
    }

}
