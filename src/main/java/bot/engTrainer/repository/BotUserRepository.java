package bot.engTrainer.repository;

import bot.engTrainer.entities.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {

    Optional<BotUser> findByEmail(String identifier);
    Optional<BotUser> findByName(String identifier);
    Optional<BotUser> findByChatId(Long chatId);


}
