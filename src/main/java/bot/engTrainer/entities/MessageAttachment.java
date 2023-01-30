package bot.engTrainer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "message_attachments")
@Getter
@Setter
@NoArgsConstructor
public class MessageAttachment {

    @ManyToOne
    @JoinColumn(name = "message")
    private ExtMessage message;
    @Column
    private String identifier;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "files_ize")
    private Long fileSize;

    @Lob
    @Column(name = "data")
    private Blob data;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;
    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createTime;

}
