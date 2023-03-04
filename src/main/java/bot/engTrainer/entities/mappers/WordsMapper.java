package bot.engTrainer.entities.mappers;

import bot.engTrainer.entities.Words;
import bot.engTrainer.entities.dto.WordDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordsMapper {

    WordDto toDto(Words word);

}
