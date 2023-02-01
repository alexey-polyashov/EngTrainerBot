package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
public class Words {

    @Id
    @Column(name = "id")
    private Long id;

    @Column
    private String foreign_write;

    @Column
    private String transcription;

    @Column
    private String native_write;

    @ManyToMany(mappedBy="words")
    private List<Dictionaries> dictionaries;

}
