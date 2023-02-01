package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "dictionaries")
@Getter
@Setter
@NoArgsConstructor
public class Dictionaries {


    @Column
    private String description;

    @Id
    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="dict_words",
            joinColumns=    @JoinColumn(name="dict_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="word_id", referencedColumnName="id"))
    Set<Words> words;
    
    @Column(name = "marked")
    @ColumnDefault("false")
    private Boolean marked = false;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateTime;

}
