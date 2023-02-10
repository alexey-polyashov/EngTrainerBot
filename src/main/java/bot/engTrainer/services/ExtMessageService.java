package bot.engTrainer.services;

import bot.engTrainer.config.BotConfig;
import bot.engTrainer.entities.BotUser;
import bot.engTrainer.entities.ExtMessage;
import bot.engTrainer.entities.MessageAttachment;
import bot.engTrainer.entities.dto.ExtMessageDto;
import bot.engTrainer.entities.dto.NewExtMessageDto;
import bot.engTrainer.entities.mappers.ExtMessageMapper;
import bot.engTrainer.exceptions.ResourceNotFound;
import bot.engTrainer.exceptions.BotException;
import bot.engTrainer.repository.ExtMessageRepository;
import bot.engTrainer.repository.MessageAttachmentRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtMessageService {

    private final ExtMessageRepository messageRepository;
    private final MessageAttachmentRepository attachmentRepository;
    private final ExtMessageMapper messageMapper;
    private final BotConfig botConfig;

    private static final String CRON = "*/10 * * * * *";
    private static final Long scheduleLimit = 50L;

    @Transactional
    public ExtMessageDto addMessage(NewExtMessageDto newMessage){
        ExtMessage message = messageMapper.toModel(newMessage);
        messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    public String addAttachment(UUID messageId, MultipartFile mpf) throws IOException, SQLException {
        ExtMessage extMessage = messageRepository.findById(messageId)
                .orElseThrow(()->new ResourceNotFound("Сообщение с идентификатором '" + messageId + "' не найдено"));
        MessageAttachment attachment = new MessageAttachment();
        attachment.setMessage(extMessage);
        attachment.setIdentifier(String.valueOf(extMessage.getAttachments().size()));
        attachment.setFileSize(mpf.getSize());
        attachment.setContentType(mpf.getContentType());
        attachment.getData().setBytes(0, mpf.getBytes());
        attachmentRepository.save(attachment);
        return attachment.getIdentifier();
    }

    public ExtMessageDto findMessage(UUID messageId){
        ExtMessage message = messageRepository.findById(messageId).orElseThrow(
                ()->new ResourceNotFound("Сообщение с идентификатором '" + messageId + "' не найдено")
        );
        return messageMapper.toDto(message);
    }

    public MessageAttachment getAttachmentData(UUID attachmentId){
        return null;
    }

    public List<ExtMessageDto> getMessagesQueueDto(Long limit){
        return getMessagesQueue(limit).stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ExtMessage> getMessagesQueue(Long limit){
        return messageRepository.getQueue(limit);
    }

    public Integer getMessagesQueueSze() {
        return messageRepository.getQueueSize();
    }

    @Transactional
    public void setReady(UUID uuid) {
        messageRepository.setReady(uuid);
    }

    @Transactional
    public void setPassed(UUID uuid) {
        messageRepository.setPassed(uuid);
    }

    public void sendMessage(ExtMessage message){
        if(message.getPassed() == true){
            return;
        }
        if(message.getNumsPass() > 3){
            return;
        }
        List<BotUser> recipients = new ArrayList<>();
        if(message.getBotUser()!=null){
            recipients.add(message.getBotUser());
        }
        if(recipients.size()==0){
            throw new BotException("No recipient for message: uuid - '" + message.getId() + "'");
        }
        for (BotUser botUser : recipients) {
            Long chatId = botUser.getChatId();
            if(botUser.getBlocked()
                    || botUser.getMarked()
                    || chatId==null
                    || chatId.equals(0)){
                continue;
            }
            TelegramBot bot = new TelegramBot(botConfig.getToken());
            bot.execute(new SendMessage(chatId, message.getTextMessage()));
        }
    }

    @Scheduled(cron = CRON)
    public void scheduledSender(){
        List<ExtMessage> messageList = getMessagesQueue(scheduleLimit);
        for (ExtMessage mes: messageList) {
            try {
                sendMessage(mes);
                mes.setPassed(true);
            }catch(Exception e){
                mes.setError(e.getMessage());
                log.error("scheduledSender: message uuid - {}, error - {}", mes.getId(), e.getMessage());
                continue;
            }finally {
                mes.setNumsPass(mes.getNumsPass()+1);
            }
            messageRepository.save(mes);
        }
    }

}
