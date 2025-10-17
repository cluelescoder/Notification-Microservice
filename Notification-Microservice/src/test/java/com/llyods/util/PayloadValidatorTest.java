package com.llyods.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.llyods.exception.InvalidPayloadException;
import com.llyods.model.NotificationPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class PayloadValidatorTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PayloadValidator payloadValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateWithValidPayload() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"mail\":\"test@example.com\",\"name\":\"John Doe\",\"subject\":\"Test Subject\",\"messageContent\":\"Test Message\"}";
        NotificationPayload notificationPayload = new NotificationPayload("INFO", "test@example.com", "John Doe", "Test Subject", "Test Message");

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        assertAll(
                () -> assertDoesNotThrow(() -> payloadValidator.validate(jsonPayload)),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }

    @Test
    void testValidateWithMissingEmail() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"name\":\"John Doe\",\"subject\":\"Test Subject\",\"messageContent\":\"Test Message\"}";
        NotificationPayload notificationPayload = new NotificationPayload("INFO", null, "John Doe", "Test Subject", "Test Message");

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        assertAll(
                () -> assertThatThrownBy(() -> payloadValidator.validate(jsonPayload))
                        .isInstanceOf(InvalidPayloadException.class)
                        .hasMessageContaining("Invalid message payload: Missing email"),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }

    @Test
    void testValidateWithEmptyEmail() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"mail\":\"\",\"name\":\"John Doe\",\"subject\":\"Test Subject\",\"messageContent\":\"Test Message\"}";
        NotificationPayload notificationPayload = new NotificationPayload("INFO", "", "John Doe", "Test Subject", "Test Message");

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        assertAll(
                () -> assertThatThrownBy(() -> payloadValidator.validate(jsonPayload))
                        .isInstanceOf(InvalidPayloadException.class)
                        .hasMessageContaining("Invalid message payload: Missing email"),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }

    @Test
    void testValidateWithInvalidEmailFormat() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"mail\":\"invalid-email\",\"name\":\"John Doe\",\"subject\":\"Test Subject\",\"messageContent\":\"Test Message\"}";
        NotificationPayload notificationPayload = new NotificationPayload("INFO", "invalid-email", "John Doe", "Test Subject", "Test Message");

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        assertAll(
                () -> assertThatThrownBy(() -> payloadValidator.validate(jsonPayload))
                        .isInstanceOf(InvalidPayloadException.class)
                        .hasMessageContaining("Invalid email format"),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }

    @Test
    void testValidateWithDeserializationError() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"mail\":\"test@example.com\",\"name\":\"John Doe\",\"subject\":\"Test Subject\",\"messageContent\":\"Test Message\"}";

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenThrow(new JsonProcessingException("Deserialization error") {});

        assertAll(
                () -> assertThatThrownBy(() -> payloadValidator.validate(jsonPayload))
                        .isInstanceOf(InvalidPayloadException.class)
                        .hasMessageContaining("Invalid message payload: Deserialization error"),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }

    @Test
    void testValidateWithMissingSubject() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"mail\":\"test@example.com\",\"name\":\"John Doe\",\"messageContent\":\"Test Message\"}";
        NotificationPayload notificationPayload = new NotificationPayload("INFO", "test@example.com", "John Doe", null, "Test Message");

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        assertAll(
                () -> assertThatThrownBy(() -> payloadValidator.validate(jsonPayload))
                        .isInstanceOf(InvalidPayloadException.class)
                        .hasMessageContaining("Invalid message payload: Missing subject"),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }

    @Test
    void testValidateWithEmptySubject() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"mail\":\"test@example.com\",\"name\":\"John Doe\",\"subject\":\"\",\"messageContent\":\"Test Message\"}";
        NotificationPayload notificationPayload = new NotificationPayload("INFO", "test@example.com", "John Doe", "", "Test Message");

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        assertAll(
                () -> assertThatThrownBy(() -> payloadValidator.validate(jsonPayload))
                        .isInstanceOf(InvalidPayloadException.class)
                        .hasMessageContaining("Invalid message payload: Missing subject"),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }

    @Test
    void testValidateWithMissingMessageContent() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"mail\":\"test@example.com\",\"name\":\"John Doe\",\"subject\":\"Test Subject\"}";
        NotificationPayload notificationPayload = new NotificationPayload("INFO", "test@example.com", "John Doe", "Test Subject", null);

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        assertAll(
                () -> assertThatThrownBy(() -> payloadValidator.validate(jsonPayload))
                        .isInstanceOf(InvalidPayloadException.class)
                        .hasMessageContaining("Invalid message payload: Invalid payload: Missing Content"),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }

    @Test
    void testValidateWithEmptyMessageContent() throws Exception {
        String jsonPayload = "{\"type\":\"INFO\",\"mail\":\"test@example.com\",\"name\":\"John Doe\",\"subject\":\"Test Subject\",\"messageContent\":\"\"}";
        NotificationPayload notificationPayload = new NotificationPayload("INFO", "test@example.com", "John Doe", "Test Subject", "");

        when(objectMapper.readValue(jsonPayload, NotificationPayload.class)).thenReturn(notificationPayload);

        assertAll(
                () -> assertThatThrownBy(() -> payloadValidator.validate(jsonPayload))
                        .isInstanceOf(InvalidPayloadException.class)
                        .hasMessageContaining("Invalid message payload: Invalid payload: Missing Content"),
                () -> verify(objectMapper, times(1)).readValue(jsonPayload, NotificationPayload.class)
        );
    }
    @Test
    void testIsValidEmail() {
        assertAll(
                () -> assertThat(payloadValidator.isValidEmail("test@example.com")).isTrue(),
                () -> assertThat(payloadValidator.isValidEmail("user.name@domain.co.in")).isTrue(),
                () -> assertThat(payloadValidator.isValidEmail("test@sub.domain.com")).isTrue(),
                () -> assertThat(payloadValidator.isValidEmail("invalid-email")).isFalse(),
                () -> assertThat(payloadValidator.isValidEmail("test@")).isFalse(),
                () -> assertThat(payloadValidator.isValidEmail("test@.com")).isTrue(), // As per regex
                () -> assertThat(payloadValidator.isValidEmail("testexample.com")).isFalse(),
                () -> assertThat(payloadValidator.isValidEmail("test@domain..com")).isTrue(), // As per regex
                () -> assertThat(payloadValidator.isValidEmail(null)).isFalse(),
                () -> assertThat(payloadValidator.isValidEmail("")).isFalse()
        );
    }
}
