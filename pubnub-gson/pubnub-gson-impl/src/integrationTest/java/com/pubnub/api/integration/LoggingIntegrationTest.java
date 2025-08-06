package com.pubnub.api.integration;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.integration.util.CustomLoggerTestImpl;
import com.pubnub.api.integration.util.ITTestConfig;
import com.pubnub.api.integration.util.RandomGenerator;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.logging.LogMessageContent;
import com.pubnub.api.logging.LogMessageType;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoggingIntegrationTest extends BaseIntegrationTest {
    final ITTestConfig itTestConfig = ConfigFactory.create(ITTestConfig.class, System.getenv());

    @Test
    public void testCanLogMessagesUsingCustomLogger() throws PubNubException, InterruptedException {
        // Clear any previous messages
        CustomLoggerTestImpl.clear();

        String expectedUuid = UUID.randomUUID().toString();
        String expectedChannel = "test-channel-" + RandomGenerator.newValue(8);

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(
                new UserId(expectedUuid),
                itTestConfig.subscribeKey()
        );
        configBuilder.publishKey(itTestConfig.publishKey());
        configBuilder.customLoggers(Collections.singletonList(new CustomLoggerTestImpl()));

        PubNub pubNub = PubNub.create(configBuilder.build());

        // Publish a message to generate logs
        pubNub.publish()
                .channel(expectedChannel)
                .message(generatePayload())
                .customMessageType("myType")
                .sync();

        // Verify logging
        assertFalse("Should have received string messages", CustomLoggerTestImpl.stringMessages.isEmpty());
        assertFalse("Should have received LogMessage objects", CustomLoggerTestImpl.logMessages.isEmpty());

        // Verify content - string messages
        assertTrue(
                "Should have called publish on the expected channel",
                CustomLoggerTestImpl.stringMessages.stream().anyMatch(msg ->
                        msg.contains("PublishEndpoint") && msg.contains(expectedChannel)
                )
        );

        assertTrue(
                "Should have called publish on the expected channel",
                CustomLoggerTestImpl.stringMessages.stream().anyMatch(msg ->
                        msg.contains("NetworkRequest") && msg.contains(expectedChannel)
                )
        );

        assertTrue(
                "Should have called publish on the expected channel",
                CustomLoggerTestImpl.stringMessages.stream().anyMatch(msg ->
                        msg.contains("NetworkResponse") && msg.contains(expectedChannel)
                )
        );

        // Verify content - LogMessage objects
        assertTrue(
                "Should have called publish on the expected channel",
                CustomLoggerTestImpl.logMessages.stream().anyMatch(msg ->
                        msg.getType() == LogMessageType.OBJECT &&
                                msg.getLocation().contains("PublishEndpoint") &&
                                (msg.getMessage() instanceof LogMessageContent.Object) &&
                                expectedChannel.equals(((LogMessageContent.Object) msg.getMessage()).getMessage().get("channel"))
                )
        );

        assertTrue(
                "Should have called publish API. LogMessage type should be NETWORK_REQUEST",
                CustomLoggerTestImpl.logMessages.stream().anyMatch(msg ->
                        msg.getType() == LogMessageType.NETWORK_REQUEST &&
                                msg.getLocation().contains("publish")
                )
        );

        assertTrue(
                "Should have called publish API. LogMessage type should be NETWORK_RESPONSE",
                CustomLoggerTestImpl.logMessages.stream().anyMatch(msg ->
                        msg.getType() == LogMessageType.NETWORK_RESPONSE &&
                                msg.getLocation().contains("publish")
                )
        );

        pubNub.destroy();
    }

}
