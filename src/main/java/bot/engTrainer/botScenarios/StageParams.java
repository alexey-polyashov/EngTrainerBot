package bot.engTrainer.botScenarios;

import com.github.kshashov.telegram.api.TelegramRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StageParams {
    TelegramBot bot;
    Chat chat;
    Message message;
    TelegramRequest request;
}
