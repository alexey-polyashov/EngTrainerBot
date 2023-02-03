package bot.engTrainer.entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BotUserDto {

    private Long id;
    private Long chatId;
    private String name;
    private String email;
    private Boolean marked;
    private Boolean blocked;

    List<RoleDto> roles;

}
