package com.llyods.util;

import com.llyods.exception.InvalidPayloadException;
import com.llyods.model.NotificationPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayloadValidator {

    private final ObjectMapper objectMapper;

    public PayloadValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void validate(String payload) {
        try {
            log.info("Validating JSON payload: {}", payload);
            NotificationPayload notificationPayload = objectMapper.readValue(payload, NotificationPayload.class);

            if (notificationPayload.getMail() == null || notificationPayload.getMail().isEmpty()) {
                log.error("Invalid payload: Missing email");
                throw new InvalidPayloadException("Invalid message payload: Missing email");
            }

            if (notificationPayload.getSubject() == null || notificationPayload.getSubject().isEmpty()) {
                log.error("Invalid payload: Missing subject");
                throw new InvalidPayloadException("Invalid message payload: Missing subject");
            }

            if (notificationPayload.getMessageContent() == null || notificationPayload.getMessageContent().isEmpty()) {
                log.error("Invalid payload: Missing content");
                throw new InvalidPayloadException("Invalid payload: Missing Content");
            }

            if (!isValidEmail(notificationPayload.getMail())) {
                log.error("Invalid email format: {}", notificationPayload.getMail());
                throw new InvalidPayloadException("Invalid email format: " + notificationPayload.getMail());
            }

        } catch (Exception e) {
            log.error("Error deserializing or validating payload: {}", e.getMessage());
            throw new InvalidPayloadException("Invalid message payload: " + e.getMessage());
        }
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
