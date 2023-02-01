package bot.engTrainer.entities.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WordDto {

    private String foreign_write;
    private String transcription;
    private String native_write;

}
