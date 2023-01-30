package bot.engTrainer.entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class ExtMessageDto {

    private String textMessage;
    private BotUserSimpleDto botUser;
    List<MessageAttachmentsDto> attachments;

    private Boolean passed;
    private Integer numsPass;
    private String error;

    private UUID id;
    private Timestamp createTime;
}
