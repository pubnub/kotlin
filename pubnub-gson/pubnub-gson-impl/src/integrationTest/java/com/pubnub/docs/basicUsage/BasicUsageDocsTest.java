package com.pubnub.docs.basicUsage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live test of the complete example on the Java landing page (docs/sdks/java.md). The ten ids of the page are the regions of
 * one file ({@code samples/BasicUsageApp.java}), so they run together: one call of {@code main}. The region {@code addDelay}
 * sleeps 60 seconds and cannot change, so this test takes a little over a minute. The sample is called once, as written. A
 * second client proves what the service did.
 */
@ExtendWith(LiveExtension.class)
class BasicUsageDocsTest {
    private static final String OPEN = "open";

    @Test
    void basicPubSubApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = clients.kotlinClient(OPEN, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "Hello World");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");
        Stdout out = Stdout.captureForTest();

        // Regions: basicPubSubApp, initializePubNub, createChannel, setupEventListeners, publishMessage, setupMessageListener,
        // subscribeToChannel and addDelay, all in this one file.
        com.pubnub.docs.basicUsage.samples.BasicUsageApp.main(new String[0]);

        PNMessageResult message = (PNMessageResult) arrived.await();
        JsonObject body = message.getMessage().getAsJsonObject();
        assertEquals("Hello World", body.get("msg").getAsString());
        assertEquals("yourUserId", message.getPublisher());
        assertEquals("text-message", message.getCustomMessageType());
        Kit.assertTimetoken(message.getTimetoken());

        // initializePubNub, createChannel and subscribeToChannel
        out.assertPrinted("Subscribed to channel: myChannel");
        // setupEventListeners: the status listener saw the connect and ran the publishMessage region
        out.assertPrinted("Connected to PubNub");
        assertTrue(out.lines("Connection lost").isEmpty(), "the sample reported a lost connection");
        // publishMessage
        out.assertPrinted("Message to send: {\"msg\":\"Hello World\"}");
        out.assertPrinted("Message published successfully with timetoken: " + message.getTimetoken());
        assertTrue(out.lines("Publish failed").isEmpty(), "the sample reported a failed publish");
        // setupMessageListener: the subscription of the sample got its own message
        out.assertPrinted("Received on channel: myChannel");
        out.assertPrinted("Received message: {\"msg\":\"Hello World\"}");
        out.assertPrinted("The content of the message is: Hello World");
        // addDelay
        out.assertPrinted("Waiting for messages. Program will exit in 60 seconds...");
        out.assertPrinted("Disconnected from PubNub");
        Kit.storedItem(receiver, "myChannel", message.getTimetoken());
    }
}
