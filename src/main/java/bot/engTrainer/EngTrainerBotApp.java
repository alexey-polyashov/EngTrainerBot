package bot.engTrainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("classpath:secret.properties")
@EnableScheduling
public class EngTrainerBotApp {

    public static void main(String[] args) {
        SpringApplication.run(EngTrainerBotApp.class);
    }


}
