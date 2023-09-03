package bot.engTrainer.helpers;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class NextWord {
    private String foreignWriting;
    private String transcription;
    private String description;
    private Set<String> variants;
    private Set<String> examples;


}
