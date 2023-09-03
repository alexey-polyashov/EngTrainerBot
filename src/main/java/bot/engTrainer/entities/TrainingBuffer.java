package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "training_buffer")
@Getter
@Setter
@NoArgsConstructor
public class TrainingBuffer {

    @Id
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Words word;

    @Column(name="word_state")
    private Integer wordState;

    @Column(name="in_process")
    private Boolean inProcess;

    @Column
    private Boolean processed;

    @Column
    private int progress;


}
