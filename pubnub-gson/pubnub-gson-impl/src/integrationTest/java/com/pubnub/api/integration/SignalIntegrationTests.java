package com.pubnub.api.integration;

import com.google.gson.Gson;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.integration.util.RandomGenerator;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class SignalIntegrationTests extends BaseIntegrationTest {

    @Override
    protected void onBefore() {
    }

    @Override
    protected void onAfter() {

    }

    @Test
    public void testPublishSignalMessageAsync() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final String expectedPayload = RandomGenerator.newValue(5);

        pubNub.signal(expectedPayload, expectedChannel)
                .async(result -> {
                    assertFalse(result.isFailure());
                    success.set(true);
                });

        listen(success);
    }

    @Test
    public void testPublishSignalMessageSync() throws PubNubException {
        final String expectedChannel = randomChannel();
        final String expectedPayload = RandomGenerator.newValue(5);
        final String customMessageType = "mytype";

        final PNPublishResult signalResult = pubNub.signal(expectedPayload, expectedChannel).customMessageType(customMessageType).sync();

        assertNotNull(signalResult);
    }

    @Test
    public void testReceiveSignalMessage() {
        final AtomicBoolean success = new AtomicBoolean();

        final String expectedChannel = randomChannel();
        final String expectedPayload = RandomGenerator.newValue(5);
        final String expectedCustomMessageType = "myCustomType";

        final com.pubnub.api.java.PubNub observerClient = getPubNub();

        observerClient.addListener(new SubscribeCallback() {


            @Override
            public void status(@NotNull com.pubnub.api.java.PubNub pubnub, @NotNull PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    if (status.getAffectedChannels().contains(expectedChannel)) {
                        pubNub.signal(expectedPayload, expectedChannel)
                                .customMessageType(expectedCustomMessageType)
                                .async((result) -> {
                                    assertFalse(result.isFailure());
                                    assertNotNull(result);
                                });
                    }
                }
            }

            @Override
            public void signal(@NotNull com.pubnub.api.java.PubNub pubnub, @NotNull PNSignalResult signal) {
                assertEquals(pubNub.getConfiguration().getUserId().getValue(), signal.getPublisher());
                assertEquals(expectedChannel, signal.getChannel());
                assertEquals(expectedPayload, new Gson().fromJson(signal.getMessage(), String.class));
                assertEquals(expectedCustomMessageType, signal.getCustomMessageType());
                success.set(true);
            }
        });

        observerClient.subscribe()
                .channels(Collections.singletonList(expectedChannel))
                .execute();

        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilTrue(success);
    }

    @Test
    public void testPublishSignalMessageSyncWithoutChannel() {
        try {
            pubNub.signal()
                    .message(randomChannel())
                    .sync();
        } catch (PubNubException e) {
            assertEquals(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING.getMessage(), e.getPubnubError()
                    .getMessage());
        }
    }

    @Test
    public void testPublishSignalMessageSyncWithoutMessage() {
        try {
            pubNub.signal()
                    .channel(randomChannel())
                    .sync();
        } catch (PubNubException e) {
            assertEquals(PubNubErrorBuilder.PNERROBJ_MESSAGE_MISSING.getMessage(), e.getPubnubError()
                    .getMessage());
        }
    }

    @Test
    public void testPublishSignalMessageSyncWithoutSubKey() {
        try {
//            pubNub.getConfiguration().setSubscribeKey(null);
            PubNub pubNub1 = PubNub.create(com.pubnub.api.java.v2.PNConfiguration.builder(new UserId(random()), "").build());

            pubNub1.signal(randomChannel(), random())
                    .sync();
        } catch (PubNubException e) {
            assertEquals(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING.getMessage(), e.getPubnubError()
                    .getMessage());
        }
    }
}
