package com.llyods.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llyods.exception.InvalidPayloadException;
import com.llyods.model.NotificationPayload;
import com.llyods.util.PayloadValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PubSubConsumerServiceTest {

    @Mock
    private MessageProcessorService messageProcessorService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PayloadValidator payloadValidator;

    @InjectMocks
    private PubSubConsumerService pubSubConsumerService;

    private String jsonPayload;
    private NotificationPayload notificationPayload;
    private Message<String> message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jsonPayload = "{\"type\":\"Email\",\"email\":\"demo@gmail.com\",\"name\":\"John Doe\",\"subject\":\"Test Subject\",\"messageContent\":\"Test Message\"}";
        notificationPayload = new NotificationPayload("Email", "demo@gmail.com", "John Doe", "Test Subject", "Test Message");
        message = MessageBuilder.withPayload(jsonPayload).build();
    }

    @Test
    void shouldProcessTransactionMessageSuccessfully() throws Exception {
        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);
        pubSubConsumerService.receiveTransactionCompletedMessage(message);
        assertAll(
                () -> verify(payloadValidator).validate(jsonPayload),
                () -> verify(messageProcessorService).processMessage(notificationPayload)
        );
    }

    @Test
    void shouldProcessProfileUpdateMessageSuccessfully() throws Exception {
        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);
        pubSubConsumerService.receiveProfileUpdatedMessage(message);
        assertAll(
                () -> verify(payloadValidator).validate(jsonPayload),
                () -> verify(messageProcessorService).processMessage(notificationPayload)
        );
    }

    @Test
    void shouldThrowInvalidPayloadExceptionForTransactionMessage() {
        doThrow(new InvalidPayloadException("Invalid payload")).when(payloadValidator).validate(jsonPayload);

        assertThatThrownBy(() -> pubSubConsumerService.receiveTransactionCompletedMessage(message))
                .isInstanceOf(InvalidPayloadException.class)
                .hasMessageContaining("Error processing message: Invalid payload");

        assertAll(
                () -> verify(payloadValidator).validate(jsonPayload),
                () -> verify(messageProcessorService, never()).processMessage(any(NotificationPayload.class))
        );
    }

    @Test
    void shouldThrowRuntimeExceptionForDeserializationError() throws Exception {
        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenThrow(new RuntimeException("Deserialization error"));
        assertThatThrownBy(() -> pubSubConsumerService.receiveTransactionCompletedMessage(message))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unexpected error processing message");
        assertAll(
                () -> verify(payloadValidator, never()).validate(anyString()),
                () -> verify(messageProcessorService, never()).processMessage(any(NotificationPayload.class))
        );
    }

    @Test
    void shouldThrowInvalidPayloadExceptionForOTPMessage() {
        doThrow(new InvalidPayloadException("Invalid payload")).when(payloadValidator).validate(jsonPayload);

        assertThatThrownBy(() -> pubSubConsumerService.receiveOTPdMessage(message))
                .isInstanceOf(InvalidPayloadException.class)
                .hasMessageContaining("Error processing message: Invalid payload");

        assertAll(
                () -> verify(payloadValidator).validate(jsonPayload),
                () -> verify(messageProcessorService, never()).processMessage(any(NotificationPayload.class))
        );
    }

    @Test
    void shouldProcessOTPMessageSuccessfully() throws Exception {
        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);
        pubSubConsumerService.receiveOTPdMessage(message);
        assertAll(
                () -> verify(payloadValidator).validate(jsonPayload),
                () -> verify(messageProcessorService).processMessage(notificationPayload)
        );
    }

    @Test
    void shouldProcessPasswordUpdateMessageSuccessfully() throws Exception {
        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);
        pubSubConsumerService.receivePasswordUpdateSuccessMessage(message);
        assertAll(
                () -> verify(payloadValidator).validate(jsonPayload),
                () -> verify(messageProcessorService).processMessage(notificationPayload)
        );
    }

    @Test
    void shouldThrowInvalidPayloadExceptionForRESENDOTPMessage() {
        doThrow(new InvalidPayloadException("Invalid payload")).when(payloadValidator).validate(jsonPayload);

        assertThatThrownBy(() -> pubSubConsumerService.receiveRESETOTPMessage(message))
                .isInstanceOf(InvalidPayloadException.class)
                .hasMessageContaining("Error processing message: Invalid payload");

        assertAll(
                () -> verify(payloadValidator).validate(jsonPayload),
                () -> verify(messageProcessorService, never()).processMessage(any(NotificationPayload.class))
        );
    }

    @Test
    void shouldProcessRESETOTPMessageSuccessfully() throws Exception {
        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        pubSubConsumerService.receiveRESETOTPMessage(message);

        assertAll(
                () -> verify(payloadValidator).validate(jsonPayload),
                () -> verify(messageProcessorService).processMessage(notificationPayload)
        );
    }

}
