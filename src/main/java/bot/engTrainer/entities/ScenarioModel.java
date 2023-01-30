package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "scenario")
@Getter
@Setter
@NoArgsConstructor
@IdClass(ScenarioModelPK.class)
public class ScenarioModel implements Serializable {

    @Id
    private Long chatId;
    @Id
    private String identifier;

    @Column(name = "data")
    private String data;
}
