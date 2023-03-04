package bot.engTrainer.entities.dto;

import bot.engTrainer.entities.Words;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class TrainingProgressDto {

    private WordDto word;
    private Date lastAppearance;
    private int countAppearance;
    private int progress;

    @Override
    public int hashCode() {
        return word.getId();
    }
}
