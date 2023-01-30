package bot.engTrainer.entities.mappers;

import bot.engTrainer.entities.ExtMessage;
import bot.engTrainer.entities.dto.*;
import bot.engTrainer.services.ExtMessageService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses={ExtMessageService.class})
public interface ExtMessageMapper {

    ExtMessage toModel(ExtMessageDto dto);
    ExtMessage toModel(NewExtMessageDto dto);
    ExtMessageDto toDto(ExtMessage botUser);

}
