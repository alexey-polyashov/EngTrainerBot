package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
public class TrainingIntervals {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name = "start_hour")
    @ColumnDefault("0")
    private int startHour;

    public TrainingIntervals(Long userId, int startHour) {
        this.userId = userId;
        this.startHour = startHour;
    }
}
