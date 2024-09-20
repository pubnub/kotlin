package com.pubnub.api.integration;

import com.google.gson.JsonObject;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.awaitility.Awaitility;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.assertEquals;

public class PresenceEventsIntegrationTests extends BaseIntegrationTest {

    @Test
    public void testJoinChannel() {
        final String channel = randomChannel();
        final AtomicBoolean success = new AtomicBoolean(false);

        pubNub.addListener(new SubscribeCallback() {

            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
                if (presence.getEvent().equals("join")) {
                    assertEquals(channel, presence.getChannel());
                    pubNub.removeListener(this);
                    success.set(true);
                }
            }

            
        });

        subscribeToChannel(pubNub, channel);

        listen(success);
    }

    @Test
    public void testLeaveChannel() {
        final AtomicBoolean success = new AtomicBoolean(false);
        final String channel = randomChannel();

        final PubNub guestClient = getPubNub();

        this.pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
                if (presence.getEvent().equals("leave")) {
                    assertEquals(channel, presence.getChannel());
                    success.set(true);
                }
            }
        });

        listen(success, () -> {
            subscribeToChannel(pubNub, channel);
            subscribeToChannel(guestClient, channel);
            unsubscribeFromChannel(guestClient, channel);
            return success.get();
        });
    }

    @Test
    public void testTimeoutFromChannel() {
        final AtomicBoolean success = new AtomicBoolean(false);
        pubNub = getPubNub(builder -> {
            builder.presenceTimeout(20);
            builder.heartbeatInterval(0);
        });

        assertEquals(20, pubNub.getConfiguration().getPresenceTimeout());
        assertEquals(0, pubNub.getConfiguration().getHeartbeatInterval());

        final String channel = random();
        final int waitTime = 21;

        pubNub.addListener(new SubscribeCallback() {

            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
                if (presence.getEvent().equals("timeout")) {
                    assertEquals(channel, presence.getChannel());
                    success.set(true);
                }
            }
        });

        subscribeToChannel(pubNub, channel);

        Awaitility.await()
                .atMost(waitTime + 1, TimeUnit.SECONDS)
                .with()
                .pollDelay(waitTime, TimeUnit.SECONDS)
                .untilTrue(success);
    }

    @Test
    public void testStateChangeEvent() {
        final AtomicBoolean success = new AtomicBoolean(false);

        final JsonObject state = generatePayload();
        final String channel = randomChannel();

        subscribeToChannel(pubNub, channel);

        pubNub.addListener(new SubscribeCallback() {

            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
                if (presence.getEvent().equals("state-change") && presence.getUuid()
                        .equals(pubNub.getConfiguration().getUserId().getValue())) {
                    assertEquals("state-change", presence.getEvent());
                    pubNub.removeListener(this);
                    success.set(true);
                }

            }
        });

        pubNub.setPresenceState()
                .channels(Collections.singletonList(channel))
                .state(state)
                .async((result) -> {

                });

        Awaitility.await().atMost(4, TimeUnit.SECONDS).untilTrue(success);
    }
}
