package com.llyods.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Slf4j
@Configuration
public class PubSubConfig {

    @Bean
    public MessageChannel transactionInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel profileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel otpInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel passwordUpdateInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel resetotpInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter transactionAdapter(
            PubSubTemplate pubSubTemplate,
            @Value("${pubsub.subscription.transaction}") String subscriptionName,
            MessageChannel transactionInputChannel) {

        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);
        adapter.setOutputChannel(transactionInputChannel);
        log.info("Created PubSubInboundChannelAdapter for transaction subscription: {}", subscriptionName);
        return adapter;
    }

    @Bean
    public PubSubInboundChannelAdapter profileAdapter(
            PubSubTemplate pubSubTemplate,
            @Value("${pubsub.subscription.profile}") String subscriptionName,
            MessageChannel profileInputChannel) {

        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);
        adapter.setOutputChannel(profileInputChannel);
        log.info("Created PubSubInboundChannelAdapter for profile subscription: {}", subscriptionName);
        return adapter;
    }

    @Bean
    public PubSubInboundChannelAdapter otpAdapter(
            PubSubTemplate pubSubTemplate,
            @Value("${pubsub.subscription.otp}") String subscriptionName,
            MessageChannel otpInputChannel) {

        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);
        adapter.setOutputChannel(otpInputChannel);
        log.info("Created PubSubInboundChannelAdapter for otp subscription: {}", subscriptionName);
        return adapter;
    }

    @Bean
    public PubSubInboundChannelAdapter passwordUpdateSuccessAdapter(
            PubSubTemplate pubSubTemplate,
            @Value("${pubsub.subscription.passwordUpdate}") String subscriptionName,
            MessageChannel passwordUpdateInputChannel) {

        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);
        adapter.setOutputChannel(passwordUpdateInputChannel);
        log.info("Created PubSubInboundChannelAdapter for password update success subscription: {}", subscriptionName);
        return adapter;
    }

    @Bean
    public PubSubInboundChannelAdapter resetotpAdapter(
            PubSubTemplate pubSubTemplate,
            @Value("${pubsub.subscription.resetotp}") String subscriptionName,
            MessageChannel resetotpInputChannel) {

        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);
        adapter.setOutputChannel(resetotpInputChannel);
        log.info("Created PubSubInboundChannelAdapter for resetotp subscription: {}", subscriptionName);
        return adapter;
    }

}
