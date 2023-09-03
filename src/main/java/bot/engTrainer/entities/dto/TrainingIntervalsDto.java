package bot.engTrainer.entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainingIntervalsDto {

    private Long userId;
    private int startHour;

}
