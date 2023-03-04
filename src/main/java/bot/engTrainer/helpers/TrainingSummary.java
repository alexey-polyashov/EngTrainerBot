package bot.engTrainer.helpers;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class TrainingSummary {
    private int newWords;
    private int studyWords;
    private int examWords;
    private int rightAnswers;
    private int wrongAnswers;
}
