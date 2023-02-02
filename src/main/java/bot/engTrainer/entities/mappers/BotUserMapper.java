package bot.engTrainer.entities.mappers;

import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.dto.BotUserDto;
import bot.engTrainer.entities.dto.BotUserUpdateDto;
import bot.engTrainer.entities.dto.NewBotUserDto;
import bot.engTrainer.services.BotUserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={BotUserService.class})
public interface BotUserMapper {

    BotUser toModel(NewBotUserDto dto);

    @Mapping(target = "id", source = "botUserSource.id")
    @Mapping(target = "email", source = "botUserUpdate.email")
    BotUser copy(BotUser botUserSource, BotUserUpdateDto botUserUpdate);
    BotUser toModel(BotUserUpdateDto dto);
    BotUserDto toDto(BotUser botUser);

}
