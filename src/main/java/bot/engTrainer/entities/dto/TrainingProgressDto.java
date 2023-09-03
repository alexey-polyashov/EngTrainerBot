package bot.engTrainer.entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class TrainingProgressDto {

    private Long userId;
    private WordDto word;
    private Date lastAppearanceDate;
    private Integer progress;
    private Integer learningInterval;

    @Override
    public int hashCode() {
        return word.getId();
    }
}