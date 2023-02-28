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

    @Column(name="count_appearance")
    private int countAppearance;

    @Column(name="in_process")
    private Boolean inProcess;

    @Column
    private Boolean processed;

    @Column(name="correct_answers")
    private Integer correctAnswers;

    @Column(name="hit_number")
    private Integer hitNumber;

    @Column
    private int progress;


}
