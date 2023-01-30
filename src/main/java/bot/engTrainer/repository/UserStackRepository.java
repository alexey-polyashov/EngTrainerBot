package bot.engTrainer.repository;

import bot.engTrainer.entities.UserStack;
import bot.engTrainer.entities.UserStackPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStackRepository extends JpaRepository<UserStack, UserStackPK> {

    List<UserStack> findByChatIdOrderByNumber(Long chatId);
    long deleteByChatId(Long chatId);

}
