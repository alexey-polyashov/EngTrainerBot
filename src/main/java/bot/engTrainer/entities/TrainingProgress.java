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

    @OneToMany(mappedBy = "bot_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BotUser> comments=new ArrayList<>();

    @Column
    private Words word;

    @Column
    private int progress;

}
