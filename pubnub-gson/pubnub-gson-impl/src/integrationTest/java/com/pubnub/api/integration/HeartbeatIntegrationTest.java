package com.pubnub.api.integration;

import com.google.gson.JsonObject;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.hamcrest.core.IsEqual;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class HeartbeatIntegrationTest extends BaseIntegrationTest {

    private String expectedChannel;

    @Override
    protected void onBefore() {
        expectedChannel = randomChannel();
    }

    @Override
    protected void onAfter() {

    }

    @Test
    public void testStateWithHeartbeat() {
        final AtomicInteger hits = new AtomicInteger();
        final JsonObject expectedStatePayload = generatePayload();
        final com.pubnub.api.java.PubNub observer = getPubNub();

        pubNub = getPubNub(builder -> {
            builder.presenceTimeout(20);
            builder.heartbeatInterval(4);
        });
        observer.addListener(new SubscribeCallback.BaseSubscribeCallback() {
            @Override
            public void status(@NotNull com.pubnub.api.java.PubNub pn, @NotNull PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    if (status.getAffectedChannels().contains(expectedChannel)) {
                        pubNub.subscribe()
                                .channels(Collections.singletonList(expectedChannel))
                                .withPresence()
                                .execute();
                    }
                }
            }

            @Override
            public void presence(@NotNull PubNub p, @NotNull PNPresenceEventResult presence) {
                if (presence.getUuid().equals(pubNub.getConfiguration().getUserId().getValue())
                        && presence.getChannel().equals(expectedChannel)) {
                    switch (presence.getEvent()) {
                        case "state-change":
                            assertEquals(expectedStatePayload, presence.getState());
                            hits.incrementAndGet();
                            pubNub.disconnect();
                            break;
                        case "join":
                            if (presence.getState() == null) {
                                hits.incrementAndGet();

                                final AtomicBoolean stateSet = new AtomicBoolean();

                                pubNub.setPresenceState()
                                        .state(expectedStatePayload)
                                        .channels(Collections.singletonList(expectedChannel))
                                        .async((result) -> {
                                            assertFalse(result.isFailure());
                                            assertEquals(expectedStatePayload, result.getOrNull().getState());
                                            hits.incrementAndGet();
                                            stateSet.set(true);
                                        });

                                Awaitility.await().atMost(Durations.FIVE_SECONDS).untilTrue(stateSet);

                            } else {
                                assertEquals(expectedStatePayload, presence.getState());
                                hits.incrementAndGet();
                            }
                            break;
                        case "timeout":
                        case "leave":
                            pubNub.reconnect();
                            break;
                    }
                }
            }
        });

        observer.subscribe()
                .channels(Collections.singletonList(expectedChannel))
                .withPresence()
                .execute();

        Awaitility.await()
                .atMost(Duration.ofSeconds(40))
                .untilAtomic(hits, IsEqual.equalTo(4));
    }

}
