package bot.engTrainer.entities.mappers;

import bot.engTrainer.entities.TrainingBuffer;
import bot.engTrainer.entities.dto.TrainingBufferDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingBufferMapper {

    TrainingBufferDto toDto(TrainingBuffer progress);
    TrainingBuffer toModel(TrainingBufferDto progressDto);

}
