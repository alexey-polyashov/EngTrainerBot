package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
public class TrainingProgress {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    private Words word;

    @Column
    private int progress;

}
