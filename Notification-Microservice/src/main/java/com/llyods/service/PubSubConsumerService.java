package com.llyods.service;

import com.llyods.exception.InvalidPayloadException;
import com.llyods.exception.MessageProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.llyods.model.NotificationPayload;
import com.llyods.util.PayloadValidator;

@Service
@Slf4j
public class PubSubConsumerService {

    private final MessageProcessorService messageProcessorService;
    private final ObjectMapper objectMapper;
    private final PayloadValidator payloadValidator;

    public PubSubConsumerService(MessageProcessorService messageProcessorService, ObjectMapper objectMapper, PayloadValidator payloadValidator) {
        this.messageProcessorService = messageProcessorService;
        this.objectMapper = objectMapper;
        this.payloadValidator = payloadValidator;
    }

    @ServiceActivator(inputChannel = "transactionInputChannel")
    public void receiveTransactionCompletedMessage(Message<String> message) {
        log.info("Received transaction message: {}", message.getPayload());
        processMessage(message);
    }

    @ServiceActivator(inputChannel = "profileInputChannel")
    public void receiveProfileUpdatedMessage(Message<String> message) {
        log.info("Received profile update message: {}", message.getPayload());
        processMessage(message);
    }

    @ServiceActivator(inputChannel = "otpInputChannel")
    public void receiveOTPdMessage(Message<String> message) {
        log.info("Received OTP message: {}", message.getPayload());
        processMessage(message);
    }

    @ServiceActivator(inputChannel = "passwordUpdateInputChannel")
    public void receivePasswordUpdateSuccessMessage(Message<String> message) {
        log.info("Received Password Update Success message: {}", message.getPayload());
        processMessage(message);
    }

    @ServiceActivator(inputChannel = "resetotpInputChannel")
    public void receiveRESETOTPMessage(Message<String> message) {
        log.info("Received RESET OTP message: {}", message.getPayload());
        processMessage(message);
    }

    private void processMessage(Message<String> message) {
        try {
            String payload = message.getPayload();
            log.info("Received payload: {}", payload);
            NotificationPayload notificationPayload = objectMapper.readValue(payload, NotificationPayload.class);
            payloadValidator.validate(payload);
            messageProcessorService.processMessage(notificationPayload);

        } catch (InvalidPayloadException e) {
            log.error("Invalid payload: {}", e.getMessage());
            throw new InvalidPayloadException("Error processing message: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error deserializing or processing the message: {}", e.getMessage(), e); // Logs any other errors
            throw new MessageProcessingException("Unexpected error processing message");
        }
    }
}