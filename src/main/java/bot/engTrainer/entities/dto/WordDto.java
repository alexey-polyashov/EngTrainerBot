package bot.engTrainer.entities.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WordDto {

    private Integer id;
    private String foreignWrite;
    private String transcription;
    private String nativeWrite;
    private String synonyms;
    private String description;

}
