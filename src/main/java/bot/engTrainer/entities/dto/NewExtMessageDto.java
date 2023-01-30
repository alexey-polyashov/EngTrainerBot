package bot.engTrainer.entities.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class NewExtMessageDto {

    private String textMessage;
    private String service;
    private String userIdentifier;//логин, email

}
