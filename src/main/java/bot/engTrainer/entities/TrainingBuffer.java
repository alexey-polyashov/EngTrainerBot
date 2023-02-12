package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "training_buffer")
@Getter
@Setter
@NoArgsConstructor
public class TrainingBuffer {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private BotUser botUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private Words word;

    @Column(name="last_appearance")
    private Date lastAppearance;

    @Column(name="count_appearance")
    private int countAppearance;

    @Column
    private int progress;

}
