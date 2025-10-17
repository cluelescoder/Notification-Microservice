package com.llyods.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPayloadException.class)
    public String handleInvalidPayloadException(InvalidPayloadException ex) {
        log.error("InvalidPayloadException occurred: {}", ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(MessageProcessingException.class)
    public String handleMessageProcessingException(MessageProcessingException ex) {
        log.error("MessageProcessingException occurred: {}", ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(EmailDeliveryException.class)
    public String handleEmailDeliveryException(EmailDeliveryException ex) {
        log.error("EmailDeliveryException occurred: {}", ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        return "Internal Server Error: " + ex.getMessage();
    }
}
