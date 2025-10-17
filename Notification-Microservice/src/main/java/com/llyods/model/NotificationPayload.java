package com.llyods.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

public class NotificationPayload {

    @NotNull(message = "Notification type cannot be null")
    private String type;

    @Email(message = "Invalid email format")
    @NotNull(message = "Email cannot be null")
    private String mail;

    @NotNull(message = "Recipient name cannot be null")
    private String name;

    @NotNull(message = "Subject cannot be null")
    private String subject;

    @NotNull(message = "Message content cannot be null")
    private String messageContent;

    public NotificationPayload(String type, String mail, String name, String subject, String messageContent) {
        this.type=type;
        this.mail = mail;
        this.name = name;
        this.subject = subject;
        this.messageContent = messageContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
