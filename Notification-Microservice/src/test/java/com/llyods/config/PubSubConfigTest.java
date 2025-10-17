package com.llyods.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageChannel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PubSubConfigTest {

    @Mock
    private PubSubTemplate pubSubTemplate;

    @Mock
    private MessageChannel transactionInputChannel;

    @Mock
    private MessageChannel profileInputChannel;

    @Mock
    private MessageChannel otpInputChannel;

    @Mock
    private MessageChannel passwordUpdateInputChannel;

    @InjectMocks
    private PubSubConfig pubSubConfig;

    private static final String TRANSACTION_SUBSCRIPTION = "transaction-subscription";
    private static final String PROFILE_SUBSCRIPTION = "profile-subscription";
    private static final String OTP_SUBSCRIPTION = "otp-subscription";
    private static final String PASSWORD_UPDATE_SUBSCRIPTION = "passwordUpdate-subscription";

    @Test
    void testTransactionAdapter() {
        PubSubInboundChannelAdapter adapter = pubSubConfig.transactionAdapter(pubSubTemplate, TRANSACTION_SUBSCRIPTION, transactionInputChannel);
        assertAll(
                () -> assertThat(adapter).isNotNull(),
                () -> assertThat(adapter.getOutputChannel()).isEqualTo(transactionInputChannel),
                () -> verify(transactionInputChannel, never()).send(any())
        );
    }

    @Test
    void testProfileAdapter() {
        PubSubInboundChannelAdapter adapter = pubSubConfig.profileAdapter(pubSubTemplate, PROFILE_SUBSCRIPTION, profileInputChannel);
        assertAll(
                () -> assertThat(adapter).isNotNull(),
                () -> assertThat(adapter.getOutputChannel()).isEqualTo(profileInputChannel),
                () -> verify(profileInputChannel, never()).send(any())
        );
    }

    @Test
    void testOtpAdapter() {
        PubSubInboundChannelAdapter adapter = pubSubConfig.otpAdapter(pubSubTemplate, OTP_SUBSCRIPTION, otpInputChannel);
        assertAll(
                () -> assertThat(adapter).isNotNull(),
                () -> assertThat(adapter.getOutputChannel()).isEqualTo(otpInputChannel),
                () -> verify(otpInputChannel, never()).send(any())
        );
    }

    @Test
    void testPasswordUpdateSuccessAdapter() {
        PubSubInboundChannelAdapter adapter = pubSubConfig.passwordUpdateSuccessAdapter(pubSubTemplate, PASSWORD_UPDATE_SUBSCRIPTION, passwordUpdateInputChannel);
        assertAll(
                () -> assertThat(adapter).isNotNull(),
                () -> assertThat(adapter.getOutputChannel()).isEqualTo(passwordUpdateInputChannel),
                () -> verify(passwordUpdateInputChannel, never()).send(any())
        );
    }

    @Test
    void testTransactionInputChannel() {
        MessageChannel channel = pubSubConfig.transactionInputChannel();
        assertThat(channel).isNotNull();
    }

    @Test
    void testProfileInputChannel() {
        MessageChannel channel = pubSubConfig.profileInputChannel();
        assertThat(channel).isNotNull();
    }

    @Test
    void testOtpInputChannel() {
        MessageChannel channel = pubSubConfig.otpInputChannel();
        assertThat(channel).isNotNull();
    }

    @Test
    void testPasswordUpdateInputChannel() {
        MessageChannel channel = pubSubConfig.passwordUpdateInputChannel();
        assertThat(channel).isNotNull();
    }
}
