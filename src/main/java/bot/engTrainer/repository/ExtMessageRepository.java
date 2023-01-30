package bot.engTrainer.repository;

import bot.engTrainer.entities.ExtMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExtMessageRepository extends JpaRepository<ExtMessage, UUID> {

    @Query("Select count(*) From ExtMessage m Where m.ready=true AND m.passed=false AND m.numsPass<4")
    Integer getQueueSize();

    @Query(value ="Select * From external_messages AS m Where m.ready=true AND m.passed=false AND m.num_pass<4 Order By m.created_at desc Limit :limit", nativeQuery = true)
    List<ExtMessage> getQueue(@Param("limit") Long limit);

    @Modifying
    @Query("Update ExtMessage m Set m.ready=true Where m.id=:uuid")
    void setReady(@Param("uuid") UUID uuid);

    @Modifying
    @Query("Update ExtMessage m Set m.passed=true Where m.id=:uuid")
    void setPassed(@Param("uuid") UUID uuid);

}
