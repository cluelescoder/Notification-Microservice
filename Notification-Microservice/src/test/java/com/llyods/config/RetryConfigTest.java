package com.llyods.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class RetryConfigTest {

    @InjectMocks
    private RetryConfig retryConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(retryConfig, "maxAttempts", 3);
        ReflectionTestUtils.setField(retryConfig, "delay", 1000L);
        ReflectionTestUtils.setField(retryConfig, "multiplier", 2.0);
        ReflectionTestUtils.setField(retryConfig, "maxDelay", 10000L);
    }

    @Test
    void testRetryTemplateConfiguration() {
        RetryTemplate retryTemplate = retryConfig.retryTemplate();

        SimpleRetryPolicy retryPolicy = (SimpleRetryPolicy) ReflectionTestUtils.getField(retryTemplate, "retryPolicy");
        ExponentialBackOffPolicy backOffPolicy = (ExponentialBackOffPolicy) ReflectionTestUtils.getField(retryTemplate, "backOffPolicy");

        assertAll(
                () -> assertThat(retryPolicy.getMaxAttempts()).isEqualTo(3),
                () -> assertThat(backOffPolicy.getInitialInterval()).isEqualTo(1000L),
                () -> assertThat(backOffPolicy.getMultiplier()).isEqualTo(2.0),
                () -> assertThat(backOffPolicy.getMaxInterval()).isEqualTo(10000L)
        );
    }
}
