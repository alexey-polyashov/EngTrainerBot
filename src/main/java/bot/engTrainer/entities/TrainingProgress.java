package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "training_progress")
@Getter
@Setter
@NoArgsConstructor
public class TrainingProgress {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Words word;

    @Column(name="last_appearance")
    private Date lastAppearance;

    @Column(name="count_appearance")
    private int countAppearance;

    @Column
    private int progress;

}
