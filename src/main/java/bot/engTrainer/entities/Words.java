package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name="foreign_write")
    private String foreignWrite;

    @Column
    private String transcription;

    @Column(name="native_write")
    private String nativeWrite;

    @Column
    private String description;

    @ManyToMany
    private List<Dictionaries> dictionaries;

}
