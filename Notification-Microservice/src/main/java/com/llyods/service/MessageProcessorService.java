package com.llyods.service;

import com.llyods.exception.EmailDeliveryException;
import com.llyods.exception.MessageProcessingException;
import com.llyods.model.NotificationPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MessageProcessorService {

    private final PubSubMailSenderService mailSenderService;

    public MessageProcessorService(PubSubMailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    public void processMessage(NotificationPayload payload) {
        String toEmail = payload.getMail();
        String subject = payload.getSubject();

        log.info("Processing message to email: {}, subject: {}", toEmail, subject);

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", payload.getName());
            variables.put("message", payload.getMessageContent());
            mailSenderService.sendEmail(payload);
        } catch (EmailDeliveryException e) {
            log.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new MessageProcessingException("Failed to process message for email: " + toEmail);
        } catch (Exception e) {
            log.error("Unexpected error while processing message for email: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new MessageProcessingException("Unexpected error occurred while processing message for email: " + toEmail);
        }
    }
}
