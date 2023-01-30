package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "external_messages")
@Getter
@Setter
@NoArgsConstructor
public class ExtMessage {

    @Column
    private String textMessage = "";

    @ManyToOne
    @JoinColumn(name = "bot_user")
    private BotUser botUser;

    @OneToMany(cascade=ALL, mappedBy="message")
    List<MessageAttachment> attachments;

    @Column
    private Boolean ready = false;
    @Column
    private Boolean passed = false;
    @Column(name = "num_pass")
    private Integer numsPass = 0;
    @Column
    private String error = "";

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;
    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createTime;

}
