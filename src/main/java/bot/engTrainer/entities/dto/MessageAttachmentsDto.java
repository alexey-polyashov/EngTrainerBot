package bot.engTrainer.entities.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MessageAttachmentsDto {

    private String identifier;
    private UUID id;
    private String contentType;
    private Long fileSize;

}
