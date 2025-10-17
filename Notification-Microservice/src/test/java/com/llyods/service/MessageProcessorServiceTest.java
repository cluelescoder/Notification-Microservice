package com.llyods.service;

import com.llyods.exception.EmailDeliveryException;
import com.llyods.exception.MessageProcessingException;
import com.llyods.model.NotificationPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class MessageProcessorServiceTest {

    @Mock
    private PubSubMailSenderService mailSenderService;

    @InjectMocks
    private MessageProcessorService messageProcessorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    NotificationPayload payload = new NotificationPayload("demo@gmail.com", "Subject", "Message", "John Doe", "SomeOtherValue");

    @Test
    void testProcessMessageSuccessfully() {
        doNothing().when(mailSenderService).sendEmail(payload);

        assertAll(
                () -> assertDoesNotThrow(() -> messageProcessorService.processMessage(payload)),
                () -> verify(mailSenderService, times(1)).sendEmail(payload)
        );
    }

    @Test
    void testProcessMessageThrowsEmailDeliveryException() {
        doThrow(new EmailDeliveryException("Email sending failed")).when(mailSenderService).sendEmail(payload);

        assertAll(
                () -> assertThatThrownBy(() -> messageProcessorService.processMessage(payload))
                        .isInstanceOf(MessageProcessingException.class)
                        .hasMessageContaining("Failed to process message for email: " + payload.getMail()), // Ensure correct email is checked
                () -> verify(mailSenderService, times(1)).sendEmail(payload)
        );
    }


    @Test
    void testProcessMessageThrowsUnexpectedException() {
        doThrow(new RuntimeException("Unexpected error")).when(mailSenderService).sendEmail(payload);

        assertAll(
                () -> assertThatThrownBy(() -> messageProcessorService.processMessage(payload))
                        .isInstanceOf(MessageProcessingException.class)
                        .hasMessageContaining("Unexpected error occurred while processing message for email: " + payload.getMail()), // Dynamically get the expected email
                () -> verify(mailSenderService, times(1)).sendEmail(payload)
        );
    }

}
