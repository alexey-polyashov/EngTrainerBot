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

    @Column(name="last_appearance_date")
    private Date lastAppearanceDate;

    @Column(name="learning_interval")
    private Integer learningInterval; // 1- 15 minutes, 2- 8 hours, 3- 24 hours, 4- 2 weeks

    @Column
    private Integer progress; //count of success answer on each interval, min-0, max-3

}
