package com.llyods.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleInvalidPayloadException() {
        InvalidPayloadException exception = new InvalidPayloadException("Invalid payload");
        String response = globalExceptionHandler.handleInvalidPayloadException(exception);
        assertThat(response).isEqualTo("Invalid payload");
    }

    @Test
    void testHandleMessageProcessingException() {
        MessageProcessingException exception = new MessageProcessingException("Message Processing Exception");
        String response = globalExceptionHandler.handleMessageProcessingException(exception);
        assertThat(response).isEqualTo("Message Processing Exception");
    }

    @Test
    void testHandleEmailDeliveryException() {
        EmailDeliveryException exception = new EmailDeliveryException("Email Delivery Exception");
        String response = globalExceptionHandler.handleEmailDeliveryException(exception);
        assertThat(response).isEqualTo("Email Delivery Exception");
    }

    @Test
    void testHandleGlobalException() {
        Exception exception = new Exception("General error");
        String response = globalExceptionHandler.handleGlobalException(exception);
        assertThat(response).isEqualTo("Internal Server Error: General error");
    }
}
