package bot.engTrainer.helpers;

import bot.engTrainer.entities.dto.WordDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class NextWord {
    private String translate;
    private String nativeWriting;
    private String transcription;
    private String description;
    private Set<WordDto> variants;
    private Set<String> examples;


}
