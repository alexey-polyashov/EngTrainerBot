package bot.engTrainer.services;

import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.Roles;
import bot.engTrainer.entities.dto.BotUserDto;
import bot.engTrainer.entities.dto.BotUserUpdateDto;
import bot.engTrainer.entities.dto.NewBotUserDto;
import bot.engTrainer.entities.dto.RoleDto;
import bot.engTrainer.entities.mappers.BotUserMapper;
import bot.engTrainer.exceptions.ResourceNotFound;
import bot.engTrainer.repository.BotUserRepository;
import bot.engTrainer.repository.RoleRepository;
import com.pengrad.telegrambot.model.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotUserService {

    private final BotUserMapper botUserMapper;
    private final RoleRepository roleRepository;
    private final BotUserRepository botUserRepository;


    public BotUser getUserByString(String identifier){
        log.info("getUser: user id - {}", identifier);
        return getUser(identifier, new Chat())
                .orElseThrow(()->new ResourceNotFound("Пользователь с логином или email '" + identifier + "' не найден в базе"));
    }

    public Optional<BotUser> getUser(String identifier, Chat chat){
        log.info("chat:{}, getUser: user id - {}", chat.id(), identifier);
        Optional<BotUser> botUser = botUserRepository.findByName(identifier);
        if(botUser.isEmpty()){
            log.info("chat:{}, getUser: user id - {}, don't find by name", chat.id(), identifier);
            botUser = botUserRepository.findByEmail(identifier);
        }
        return botUser;
    }

    public Optional<BotUser> getUserByChat(Chat chat){
        log.info("chat:{}, getUserByChat", chat.id());
        Optional<BotUser> botUser = botUserRepository.findByChatId(chat.id());
        if(botUser.isEmpty()){
            log.info("chat:{}, getUserByChat, don't find", chat.id());
        }
        return botUser;
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

}
