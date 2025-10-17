package com.llyods.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

@Slf4j
@Configuration
@EnableRetry
public class RetryConfig {

    @Value("${retry.maxAttempts}")
    private int maxAttempts;

    @Value("${retry.delay}")
    private long delay;

    @Value("${retry.multiplier}")
    private double multiplier;

    @Value("${retry.maxDelay}")
    private long maxDelay;

    @Bean
    public RetryTemplate retryTemplate() {
        log.info("Configuring RetryTemplate with maxAttempts: {}, delay: {}, multiplier: {}, maxDelay: {}",
                maxAttempts, delay, multiplier, maxDelay);

        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempts);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(delay);
        backOffPolicy.setMultiplier(multiplier);
        backOffPolicy.setMaxInterval(maxDelay);

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}
