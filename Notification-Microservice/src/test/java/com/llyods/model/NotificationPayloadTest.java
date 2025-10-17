package com.llyods.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class NotificationPayloadTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testConstructorAndGetters() {
        NotificationPayload payload = new NotificationPayload(
                "resetlink", "test@example.com", "Test User", "Test Subject", "Test Message"
        );

        assertAll(
                () -> assertThat(payload.getType()).isEqualTo("resetlink"),
                () -> assertThat(payload.getMail()).isEqualTo("test@example.com"),
                () -> assertThat(payload.getName()).isEqualTo("Test User"),
                () -> assertThat(payload.getSubject()).isEqualTo("Test Subject"),
                () -> assertThat(payload.getMessageContent()).isEqualTo("Test Message")
        );
    }

    @Test
    void testSetters() {
        NotificationPayload payload = new NotificationPayload(
                "resetlink", "test@example.com", "Test User", "Test Subject", "Test Message"
        );
        payload.setType("resetlink");
        payload.setMail("newemail@example.com");
        payload.setName("Updated User");
        payload.setSubject("Updated Subject");
        payload.setMessageContent("Updated Message");

        assertAll(
                () -> assertThat(payload.getType()).isEqualTo("resetlink"),
                () -> assertThat(payload.getMail()).isEqualTo("newemail@example.com"),
                () -> assertThat(payload.getName()).isEqualTo("Updated User"),
                () -> assertThat(payload.getSubject()).isEqualTo("Updated Subject"),
                () -> assertThat(payload.getMessageContent()).isEqualTo("Updated Message")
        );
    }


    @Test
    void testEmailValidation() {
        NotificationPayload payload = new NotificationPayload(
                "Email", "invalid-email", "Test User", "Test Subject", "Test Message"
        );

        Set<ConstraintViolation<NotificationPayload>> violations = validator.validate(payload);

        assertAll(
                () -> assertThat(violations).hasSize(1),
                () -> assertThat(violations.iterator().next().getMessage()).isEqualTo("Invalid email format")
        );
    }

    @Test
    void testNotNullConstraints() {
        NotificationPayload payload = new NotificationPayload(
                null, null, null, null, null
        );

        Set<ConstraintViolation<NotificationPayload>> violations = validator.validate(payload);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "Notification type cannot be null",
                        "Email cannot be null",
                        "Recipient name cannot be null",
                        "Subject cannot be null",
                        "Message content cannot be null"
                );
    }
}
