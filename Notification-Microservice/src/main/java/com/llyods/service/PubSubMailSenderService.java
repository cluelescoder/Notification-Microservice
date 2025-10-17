package com.llyods.service;

import com.llyods.exception.EmailDeliveryException;
import com.llyods.model.NotificationPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PubSubMailSenderService {

    private final JavaMailSender mailSender;
    private final RetryTemplate retryTemplate;
    private final TemplateEngine templateEngine;

    @Autowired
    public PubSubMailSenderService(JavaMailSender mailSender, RetryTemplate retryTemplate, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.retryTemplate = retryTemplate;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(NotificationPayload payload) {
        log.debug("Preparing to send email to: {}, subject: {}", payload.getMail(), payload.getSubject());

        retryTemplate.execute(context -> {
            try {
                String templateName = selectTemplate(payload.getType());

                Map<String, Object> variables = new HashMap<>();
                variables.put("name", payload.getName());
                variables.put("messageContent", payload.getMessageContent());

                String htmlContent = generateEmailContent(templateName, variables);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(payload.getMail());
                helper.setSubject(payload.getSubject());
                helper.setText(htmlContent, true);

                mailSender.send(message);
                log.info("Email sent successfully to: {}", payload.getMail());
                return null;
            } catch (MailException | MessagingException e) {
                log.error("Failed to send email to: {}. Error: {}", payload.getMail(), e.getMessage());
                throw new EmailDeliveryException("Failed to send email to: " + payload.getMail());
            }
        });
    }

    private String selectTemplate(String type) {
        switch (type.toLowerCase()) {
            case "otp":
                return "otpTemplate";
            case "resetpassword":
                return "passwordResetTemplate";
            case "passwordupdatesuccess":
                return "passwordUpdateSuccessTemplate";
            case "transactioncomplete":
                return "transactionTemplate";
            default:
                return "default-template";
        }
    }

    private String generateEmailContent(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}