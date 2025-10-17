package com.llyods.service;

import com.llyods.exception.EmailDeliveryException;
import com.llyods.model.NotificationPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Properties;

@ExtendWith(MockitoExtension.class)
class PubSubMailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RetryTemplate retryTemplate;

    @Mock
    private TemplateEngine templateEngine; // Mocking Template Engine

    @InjectMocks
    private PubSubMailSenderService pubSubMailSenderService;

    @BeforeEach
    void setup() {
        mockRetryTemplate();
        mockMailSender();
    }

    private void mockRetryTemplate() {
        doAnswer(invocation -> {
            RetryCallback<?, ?> callback = invocation.getArgument(0);
            return callback.doWithRetry(null);
        }).when(retryTemplate).execute(any());
    }

    private void mockMailSender() {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties())); // Create a real MimeMessage
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendEmailSuccess() {
        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("Mocked Email Content");
        NotificationPayload payload = new NotificationPayload("otp","demo@gmail.com", "John Doe","Subject", "Message" );
        pubSubMailSenderService.sendEmail(payload);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendEmailFailure() {
        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("Mocked Email Content");
        NotificationPayload payload = new NotificationPayload("otp","demo@gmail.com", "John Doe","Subject", "Message" );
        doThrow(new RuntimeException("Mail server error")).when(mailSender).send(any(MimeMessage.class));
        assertThatThrownBy(() -> pubSubMailSenderService.sendEmail(payload))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mail server error");
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }


    @Test
    void testSendEmailWithEmptyMessageContent() {
        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("Mocked Email Content");
        NotificationPayload payload = new NotificationPayload("otp", "demo@gmail.com", "John Doe", "Subject", "");
        pubSubMailSenderService.sendEmail(payload);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendEmailWithInvalidEmailAddress() {
        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("Mocked Email Content");
        doThrow(new EmailDeliveryException("Failed to send email to: invalid-email")).when(mailSender).send(any(MimeMessage.class));
        NotificationPayload payload = new NotificationPayload("otp", "invalid-email", "John Doe", "Subject", "Message");
        assertThatThrownBy(() -> pubSubMailSenderService.sendEmail(payload))
                .isInstanceOf(EmailDeliveryException.class)
                .hasMessageContaining("Failed to send email to: invalid-email");
        verify(mailSender, times(1)).send(any(MimeMessage.class)); // Ensure send was attempted
    }

    @Test
    void testSendEmailWithDifferentTemplateTypes() {
        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("Mocked Email Content");
        NotificationPayload otpPayload = new NotificationPayload("otp", "demo@gmail.com", "John Doe", "Subject", "Message");
        pubSubMailSenderService.sendEmail(otpPayload);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        NotificationPayload resetPayload = new NotificationPayload("resetpassword", "demo@gmail.com", "John Doe", "Subject", "Message");
        pubSubMailSenderService.sendEmail(resetPayload);
        verify(mailSender, times(2)).send(any(MimeMessage.class));
        NotificationPayload passwordUpdatePayload = new NotificationPayload("passwordupdatesuccess", "demo@gmail.com", "John Doe", "Subject", "Message");
        pubSubMailSenderService.sendEmail(passwordUpdatePayload);
        verify(mailSender, times(3)).send(any(MimeMessage.class));
        NotificationPayload transactionCompletePayload = new NotificationPayload("transactioncomplete", "demo@gmail.com", "John Doe", "Subject", "Message");
        pubSubMailSenderService.sendEmail(transactionCompletePayload);
        verify(mailSender, times(4)).send(any(MimeMessage.class));
    }

    @Test
    void testSendEmailWithMailException() {
        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("Mocked Email Content");
        NotificationPayload payload = new NotificationPayload("otp", "demo@gmail.com", "John Doe", "Subject", "Message");
        doThrow(new MailException("Mail sending error") {}).when(mailSender).send(any(MimeMessage.class));
        assertThatThrownBy(() -> pubSubMailSenderService.sendEmail(payload))
                .isInstanceOf(EmailDeliveryException.class)
                .hasMessageContaining("Failed to send email to: demo@gmail.com");
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendEmailWithUnknownTemplateType() {
        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("Mocked Email Content");
        NotificationPayload unknownPayload = new NotificationPayload("unknown", "demo@gmail.com", "John Doe", "Subject", "Message");
        pubSubMailSenderService.sendEmail(unknownPayload);
        verify(mailSender, times(1)).send(any(MimeMessage.class)); // Ensuring the email is attempted to be sent
    }
}
