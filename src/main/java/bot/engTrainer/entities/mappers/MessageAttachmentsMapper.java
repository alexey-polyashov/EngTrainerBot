package bot.engTrainer.entities.mappers;


import bot.engTrainer.entities.MessageAttachment;
import bot.engTrainer.entities.dto.MessageAttachmentsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageAttachmentsMapper {

    MessageAttachmentsDto toDto(MessageAttachment model);

}
