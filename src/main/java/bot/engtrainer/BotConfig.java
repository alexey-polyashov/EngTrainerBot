package bot.engtrainer;

import com.github.kshashov.telegram.config.TelegramBotGlobalProperties;
import com.github.kshashov.telegram.config.TelegramBotGlobalPropertiesConfiguration;
import com.pengrad.telegrambot.request.SetWebhook;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class BotConfig implements TelegramBotGlobalPropertiesConfiguration {

    @Value("${telegram.bot-token}")
    private String token;
    @Value("${telegram.webhook-path}")
    private String webhook_path;

    @Override
    public void configure(TelegramBotGlobalProperties.Builder builder) {

        OkHttpClient okHttp = new OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .build();

        builder
                .setWebserverPort(8443)
                .configureBot(token, botBuilder -> {
                    botBuilder
                            .configure(builder1 -> builder1.okHttpClient(okHttp))
                            .useWebhook(new SetWebhook().url(webhook_path));
                })
                ;



    }

    public String getToken() {
        return token;
    }

}
