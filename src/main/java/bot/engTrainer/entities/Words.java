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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="foreign_write")
    private String foreignWrite;

    @Column(name="transcription")
    private String transcription;

    @Column(name="native_write")
    private String nativeWrite;

    @Column(name="description")
    private String description;

    @Column(name="synonyms")
    private String synonyms;

    @ManyToMany
    private List<Dictionaries> dictionaries;

}
