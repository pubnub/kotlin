package com.pubnub.api.integration;

import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Ignore("Large message (V2 publish) integration tests. Remove @Ignore to run on demand.")
public class LargeMessagePublishIntegrationTests extends BaseIntegrationTest {

    @Override
    protected void onBefore() {

    }

    @Override
    protected void onAfter() {

    }

    @Test
    public void testPublishLargeMessageOverV1Limit_Post() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        // Create a message larger than V1 POST limit (32,768 bytes)
        // Account for JSON quotes (2 bytes), so 40KB string ensures we exceed the limit
        final String largeMessage = generateLargeString(40_000);

        pubNub.publish()
                .message(largeMessage)
                .channel(expectedChannel)
                .usePOST(true)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertTrue(result.getOrNull().getTimetoken() > 0);
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(success);
    }

    @Test
    public void testPublishLargeMessageOverV1Limit_Get() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        // Create a message larger than V1 GET path limit (32,752 bytes)
        final String largeMessage = generateLargeString(40_000);

        // Even with usePost=false, large messages should be routed to V2 POST
        pubNub.publish(largeMessage, expectedChannel)
                .usePOST(false)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertTrue(result.getOrNull().getTimetoken() > 0);
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(success);
    }

    @Test
    public void testPublishLargeMessage_History() throws PubNubException {
        final String expectedChannel = randomChannel();
        final String largeMessage = generateLargeString(50_000);

        pubNub.publish()
                .message(largeMessage)
                .channel(expectedChannel)
                .usePOST(true)
                .sync();

        pause(3);

        final PNFetchMessagesResult historyResult = pubNub.fetchMessages()
                .channels(Collections.singletonList(expectedChannel))
                .maximumPerChannel(1)
                .sync();

        assertNotNull(historyResult);
        assertEquals(1, historyResult.getChannels().size());
        assertEquals(1, historyResult.getChannels().get(expectedChannel).size());
        assertEquals(largeMessage, historyResult.getChannels().get(expectedChannel).get(0).getMessage().getAsString());
    }

    @Test
    public void testPublishLargeMessage_Receive() throws PubNubException {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final String largeMessage = generateLargeString(50_000);

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
                // Intentionally left empty
            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                if (largeMessage.equals(message.getMessage().getAsString())) {
                    success.set(true);
                }
            }
        });

        subscribeToChannel(pubNub, expectedChannel);

        pubNub.publish()
                .message(largeMessage)
                .channel(expectedChannel)
                .usePOST(true)
                .sync();

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(success);
    }

    @Test
    public void testPublishLargeJsonObject_History() throws PubNubException, JSONException {
        final String expectedChannel = randomChannel();
        // Create a large JSON object (> 32KB)
        final JSONObject largePayload = new JSONObject();
        largePayload.put("id", random());
        largePayload.put("data", generateLargeString(40_000));
        largePayload.put("timestamp", System.currentTimeMillis());

        pubNub.publish()
                .message(largePayload)
                .channel(expectedChannel)
                .usePOST(true)
                .sync();

        pause(3);

        final PNFetchMessagesResult historyResult = pubNub.fetchMessages()
                .channels(Collections.singletonList(expectedChannel))
                .maximumPerChannel(1)
                .sync();

        assertNotNull(historyResult);
        assertEquals(1, historyResult.getChannels().size());
        assertEquals(1, historyResult.getChannels().get(expectedChannel).size());
        final JSONObject receivedMessage = new JSONObject(
                historyResult.getChannels().get(expectedChannel).get(0).getMessage().toString());
        assertEquals(largePayload.getString("id"), receivedMessage.getString("id"));
        assertEquals(largePayload.getString("data"), receivedMessage.getString("data"));
    }

    @Test
    public void testPublish100KBMessage() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final String largeMessage = generateLargeString(100_000);

        pubNub.publish()
                .message(largeMessage)
                .channel(expectedChannel)
                .usePOST(true)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertTrue(result.getOrNull().getTimetoken() > 0);
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(success);
    }

    @Test
    public void testPublish500KBMessage() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final String largeMessage = generateLargeString(500_000);

        pubNub.publish()
                .message(largeMessage)
                .channel(expectedChannel)
                .usePOST(true)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertTrue(result.getOrNull().getTimetoken() > 0);
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.ONE_MINUTE).untilTrue(success);
    }

    @Test
    public void testPublish2MBMessage() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        // We can't send exactly 2MB of raw content because the payload grows due to
        // JSON serialization overhead and encryption (if configured).
        final String largeMessage = generateLargeString(1_992_000);

        pubNub.publish(largeMessage, expectedChannel)
                .usePOST(true)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertTrue(result.getOrNull().getTimetoken() > 0);
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.ONE_MINUTE).untilTrue(success);
    }

    @Test
    public void testPublish2MBMessageWithCrypto() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        // We can't send exactly 2MB of raw content because the payload grows due to
        // JSON serialization overhead and encryption (if configured).
        final String largeMessage = generateLargeString(1_400_000);
        final PubNub pubnubWithCrypto = getPubNub(builder -> builder.cryptoModule(CryptoModule.createAesCbcCryptoModule("test", false)));


        pubnubWithCrypto.publish(largeMessage, expectedChannel)
                .usePOST(true)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertTrue(result.getOrNull().getTimetoken() > 0);
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.ONE_MINUTE).untilTrue(success);
    }

    @Test
    public void testPublishOversizedMessage_ShouldFail() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        // Create a message > 2MB which should be rejected by client side validation
        final String oversizedMessage = generateLargeString(2_500_000);

        pubNub.publish()
                .message(oversizedMessage)
                .channel(expectedChannel)
                .usePOST(true)
                .async((result) -> {
                    assertTrue(result.isFailure());
                    PubNubException exception = result.exceptionOrNull();
                    assertNotNull(exception);
                    assertEquals(PubNubError.MESSAGE_TOO_LARGE, exception.getPubnubError());
                    assertEquals(PubNubError.MESSAGE_TOO_LARGE.getMessage(), exception.getErrorMessage());
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.ONE_MINUTE).untilTrue(success);
    }

    private String generateLargeString(int size) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append('x');
        }
        return sb.toString();
    }
}