package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bot_users")
@Getter
@Setter
@NoArgsConstructor
public class BotUser {

    @Column(name="chat_id")
    @ColumnDefault("0")
    private Long chatId = 0L;
    @Column
    private String name;
    @Column
    private String email;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> roles;

    @Column
    @ColumnDefault("false")
    private Boolean blocked = false;

    @Column(name = "words_count")
    @ColumnDefault("0")
    private int wordsCount;

    @Column(name = "start_hour")
    @ColumnDefault("0")
    private int startHour;

    @Column(name = "end_hour")
    @ColumnDefault("0")
    private int endHour;

    @ManyToMany
    @JoinTable(name="select_dict",
            joinColumns=    @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="dict_id", referencedColumnName="id"))
    private Set<Dictionaries> selectDictionaries;

    @OneToMany(mappedBy="bot_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TrainingProgress> trainingProgress;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
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
