package bot.engTrainer.entities.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TrainingBufferDto {

    private Long userId;
    private WordDto word;
    private Integer wordState;
    private Boolean inProcess;
    private Boolean processed;
    private Integer progress;

    @Override
    public int hashCode() {
        return word.getId();
    }

}
