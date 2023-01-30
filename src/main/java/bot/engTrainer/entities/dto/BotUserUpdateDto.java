package bot.engTrainer.entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BotUserUpdateDto {

    private Long id;
    private String email;
    private String login;

}
