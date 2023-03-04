package bot.engTrainer.entities.mappers;

import bot.engTrainer.entities.TrainingProgress;
import bot.engTrainer.entities.dto.TrainingProgressDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingProgressMapper {

    TrainingProgressDto toDto(TrainingProgress progress);

}
