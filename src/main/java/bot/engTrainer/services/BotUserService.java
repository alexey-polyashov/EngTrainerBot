package bot.engTrainer.services;

import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.Dictionaries;
import bot.engTrainer.entities.Roles;
import bot.engTrainer.entities.TrainingIntervals;
import bot.engTrainer.entities.dto.BotUserDto;
import bot.engTrainer.entities.dto.BotUserUpdateDto;
import bot.engTrainer.entities.dto.NewBotUserDto;
import bot.engTrainer.entities.dto.RoleDto;
import bot.engTrainer.entities.mappers.BotUserMapper;
import bot.engTrainer.exceptions.ResourceNotFound;
import bot.engTrainer.repository.BotUserRepository;
import bot.engTrainer.repository.DictionaryRepository;
import bot.engTrainer.repository.RoleRepository;
import bot.engTrainer.repository.TrainingIntervalsRepository;
import com.pengrad.telegrambot.model.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotUserService {

    private final BotUserMapper botUserMapper;
    private final RoleRepository roleRepository;
    private final BotUserRepository botUserRepository;
    private final DictionaryRepository dictionaryRepository;
    private final TrainingIntervalsRepository trainingIntervalsRepository;


    public BotUserDto registerNewUser(Chat chat, String name, String email){
        Optional<BotUser> botUser = getUserByChat(chat);
        if(botUser.isPresent()){
            return botUserMapper.toDto(botUser.get());
        }
        BotUser newUser = new BotUser();
        List<Roles> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("USER").orElseThrow(()->new ResourceNotFound("Роль USER не найдена")));
        newUser.setRoles(roles);
        newUser.setName(name);
        newUser.setChatId(chat.id());
        newUser.setEmail(email);
        newUser.setMarked(false);
        botUserRepository.save(newUser);
        return  botUserMapper.toDto(newUser);
    }

    public BotUser getUserByString(String identifier){
        return getUser(identifier)
                .orElseThrow(()->new ResourceNotFound("Пользователь с логином или email '" + identifier + "' не найден в базе"));
    }

    public Optional<BotUser> getUser(String identifier){
        Optional<BotUser> botUser = botUserRepository.findByName(identifier);
        if(botUser.isEmpty()){
            botUser = botUserRepository.findByEmail(identifier);
        }
        return botUser;
    }

    public Optional<BotUser> getUserByChat(Chat chat){
        return botUserRepository.findByChatId(chat.id());
    }

    @Transactional
    public Set<TrainingIntervals> getUserTrainingIntervals(Chat chat){
        BotUser botUser = botUserRepository.findByChatId(chat.id())
                .orElseThrow(()->new ResourceNotFound("Пользователь с id '" + chat.id() + "' не найден"));
        int s = botUser.getTrainingIntervals().size();
        return botUser.getTrainingIntervals();
    }

    @Transactional
    public void addUserTrainingInterval(Chat chat, int hour){
        BotUser botUser = botUserRepository.findByChatId(chat.id())
                .orElseThrow(()->new ResourceNotFound("Пользователь с id '" + chat.id() + "' не найден"));
        for (TrainingIntervals interval:botUser.getTrainingIntervals()){
            if(interval.getStartHour()==hour) return;
        }
        TrainingIntervals interval = new TrainingIntervals(botUser.getId(), hour);
        trainingIntervalsRepository.save(interval);
    }

    @Transactional
    public void delUserTrainingIntervals(Chat chat, int hour){
        BotUser botUser = botUserRepository.findByChatId(chat.id())
                .orElseThrow(()->new ResourceNotFound("Пользователь с id '" + chat.id() + "' не найден"));
        TrainingIntervals interval = null;
        for (TrainingIntervals i:botUser.getTrainingIntervals()){
            if(i.getStartHour()==hour){
                interval=i;
            }
        }
        if(interval!=null) {
            botUser.getTrainingIntervals().remove(interval);
        }
    }

    @Transactional
    public void clearUserTrainingIntervals(Chat chat){
        BotUser botUser = botUserRepository.findByChatId(chat.id())
                .orElseThrow(()->new ResourceNotFound("Пользователь с id '" + chat.id() + "' не найден"));
        botUser.getTrainingIntervals().clear();
        botUserRepository.save(botUser);
    }


    public Long saveBotUser(BotUser botUser) {
        botUserRepository.save(botUser);
        return botUser.getId();
    }

    public Long saveBotUser(BotUserUpdateDto botUserDto) {
        Long id = botUserDto.getId();
        if(id==null || id==0){
            throw new InternalError("Должен быть указан id изменяемого объекта");
        }
        BotUser oldBotUser = botUserRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Пользователь с id '" + id + "' не найден"));
        BotUser newBotUser = botUserMapper.copy(oldBotUser, botUserDto);
        botUserRepository.save(newBotUser);
        return newBotUser.getId();
    }

    public Long addBotUser(NewBotUserDto botUserDto) {
        BotUser botUser = botUserMapper.toModel(botUserDto);
        botUserRepository.save(botUser);
        return botUser.getId();
    }

    public void saveRoles(Long userId, List<RoleDto> rolesDto) {
        BotUser oldBotUser = botUserRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFound("Пользователь с id '" + userId + "' не найден"));
        List<Roles> roles = rolesDto.stream()
                        .map((v)->roleRepository.findByName(v.getName()).orElseThrow(
                                ()->new ResourceNotFound("Роль с наименованием '" + v.getName() + "' не найденa"))
                        )
                                .collect(Collectors.toList());
        oldBotUser.setRoles(roles);
        botUserRepository.save(oldBotUser);
    }

    public void setMarkUser(Long userId, Boolean mark){
        BotUser oldBotUser = botUserRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFound("Пользователь с id '" + userId + "' не найден"));
        oldBotUser.setMarked(mark);
        botUserRepository.save(oldBotUser);
    }

    public void setBlockUser(Long userId, Boolean block){
        BotUser oldBotUser = botUserRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFound("Пользователь с id '" + userId + "' не найден"));
        oldBotUser.setBlocked(block);
        botUserRepository.save(oldBotUser);
    }

    public BotUserDto findUserById(Long userId){
        BotUser oldBotUser = botUserRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFound("Пользователь с id '" + userId + "' не найден"));
        return botUserMapper.toDto(oldBotUser);
    }

    public void deleteBotUser(Long userId){
        botUserRepository.deleteById(userId);
    }

    @Transactional
    public Set<Dictionaries> getSelectedDictionaries(Chat chat){
        BotUser user = botUserRepository.findByChatId(chat.id()).orElseThrow(()-> new ResourceNotFound("Пользователь по id чата '" + chat.id() + "' не найден"));
        user.getSelectDictionaries().size();//lazy initialization
        return user.getSelectDictionaries();
    }

    @Transactional
    public void addDictionary(Chat chat, Dictionaries dict){
        BotUser user = botUserRepository.findByChatId(chat.id()).orElseThrow(()-> new ResourceNotFound("Пользователь по id чата '" + chat.id() + "' не найден"));
        user.getSelectDictionaries().add(dict);
        botUserRepository.save(user);
    }

    @Transactional
    public void delDictionary(Chat chat, Long dictId){
        BotUser user = botUserRepository.findByChatId(chat.id()).orElseThrow(()-> new ResourceNotFound("Пользователь по id чата '" + chat.id() + "' не найден"));
        for(Dictionaries dict: user.getSelectDictionaries()){
            if(dict.getId()==dictId) {
                user.getSelectDictionaries().remove(dict);
                break;
            }
        }
        botUserRepository.save(user);
    }

}
