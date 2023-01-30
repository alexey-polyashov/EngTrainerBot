package bot.engTrainer.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user_stack")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserStackPK.class)
public class UserStack  implements Serializable {

    @Id
    private Long chatId;
    @Id
    private Integer number;

    private String scenarioId;

}
