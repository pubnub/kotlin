package com.pubnub.api.integration;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.java.PubNubForJava;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.integration.util.RandomGenerator;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.java.v2.callbacks.EventListener;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.hamcrest.core.IsEqual;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PresenceIntegrationTests extends BaseIntegrationTest {

    private static final String STATE_CHANGE_EVENT = "state-change";

    @Test
    public void testWhereNow() {
        final AtomicBoolean success = new AtomicBoolean();

        final int expectedChannelsCount = 4;

        final List<String> expectedChannels = new ArrayList<>();
        for (int i = 0; i < expectedChannelsCount; i++) {
            expectedChannels.add(RandomGenerator.get());
        }

        subscribeToChannel(pubNub, expectedChannels);

        pause(TIMEOUT_MEDIUM);

        pubNub.whereNow()
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertEquals(expectedChannelsCount, result.getOrNull().getChannels().size());
                    for (String channel : result.getOrNull().getChannels()) {
                        assertTrue(expectedChannels.contains(channel));
                    }
                    success.set(true);
                });

        listen(success);
    }

    @Test
    public void testGlobalHereNow() {
        final AtomicBoolean success = new AtomicBoolean();

        final int expectedChannelsCount = 2;
        final int expectedClientsCount = 3;

        final List<String> expectedChannels = new ArrayList<>(expectedChannelsCount);
        for (int i = 0; i < expectedChannelsCount; i++) {
            expectedChannels.add(RandomGenerator.get());
        }

        final List<PubNubForJava> clients = new ArrayList<PubNubForJava>(expectedClientsCount) {{
            add(pubNub);
        }};

        for (int i = 1; i <= expectedClientsCount - 1; i++) {
            clients.add(getPubNub());
        }

        for (PubNubForJava client : clients) {
            subscribeToChannel(client, expectedChannels);
        }

        assertEquals(expectedClientsCount, clients.size());

        for (PubNubForJava client : clients) {
            assertEquals(expectedChannelsCount, client.getSubscribedChannels().size());
        }

        pause(TIMEOUT_MEDIUM);

        pubNub.hereNow()
                .includeUUIDs(true)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    result.onSuccess( pnHereNowResult -> {
                                assertTrue(pnHereNowResult.getTotalOccupancy() >= expectedClientsCount);
                                assertTrue(pnHereNowResult.getTotalChannels() >= expectedChannelsCount);
                                assertTrue(pnHereNowResult.getChannels().size() >= expectedChannelsCount);

                                final List<String> channelsResult = new ArrayList<String>() {{
                                    addAll(pnHereNowResult.getChannels().keySet());
                                }};
                                assertTrue(channelsResult.containsAll(expectedChannels));


                                for (Map.Entry<String, PNHereNowChannelData> entry : pnHereNowResult.getChannels().entrySet()) {
                                    if (expectedChannels.contains(entry.getKey())) {
                                        assertTrue(entry.getValue().getOccupancy() >= expectedClientsCount);
                                        assertTrue(entry.getValue().getOccupants().size() >= expectedClientsCount);
                                        final List<PNHereNowOccupantData> occupants = entry.getValue().getOccupants();

                                        final List<String> resultUuidList = new ArrayList<>();
                                        for (PNHereNowOccupantData occupant : occupants) {
                                            resultUuidList.add(occupant.getUuid());
                                        }

                                        final List<String> expectedUuidList = new ArrayList<>();
                                        for (PubNubForJava client : clients) {
                                            expectedUuidList.add(client.getConfiguration().getUserId().getValue());
                                        }

                                        Collections.sort(expectedUuidList);
                                        Collections.sort(resultUuidList);

                                        assertEquals(expectedUuidList, resultUuidList);
                                    }

                                }

                                success.set(true);
                            }
                    );
                });

        listen(success);
    }

    @Test
    public void testHereNow() {
        final AtomicBoolean success = new AtomicBoolean();

        final int expectedChannelsCount = 3;
        final int expectedClientsCount = 4;

        final List<String> expectedChannels = new ArrayList<>(expectedChannelsCount);
        for (int i = 0; i < expectedChannelsCount; i++) {
            expectedChannels.add(RandomGenerator.get());
        }

        final List<PubNubForJava> clients = new ArrayList<PubNubForJava>() {{
            add(pubNub);
        }};
        for (int i = 1; i < expectedClientsCount; i++) {
            clients.add(getPubNub());
        }

        for (PubNubForJava client : clients) {
            subscribeToChannel(client, expectedChannels);
        }

        assertEquals(expectedChannelsCount, expectedChannels.size());
        assertEquals(expectedClientsCount, clients.size());

        pause(TIMEOUT_MEDIUM);

        pubNub.hereNow()
                .channels(expectedChannels)
                .includeUUIDs(true)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    result.onSuccess(pnHereNowResult -> {
                        assertEquals(expectedChannelsCount, pnHereNowResult.getTotalChannels());
                        assertEquals(expectedChannelsCount, pnHereNowResult.getChannels().size());
                        assertEquals(expectedChannelsCount * expectedClientsCount, pnHereNowResult.getTotalOccupancy());

                        for (Map.Entry<String, PNHereNowChannelData> entry : pnHereNowResult.getChannels().entrySet()) {
                            assertTrue(expectedChannels.contains(entry.getKey()));
                            assertTrue(expectedChannels.contains(entry.getValue().getChannelName()));
                            assertEquals(expectedClientsCount, entry.getValue().getOccupancy());
                            assertEquals(expectedClientsCount, entry.getValue().getOccupants().size());
                            for (PNHereNowOccupantData occupant : entry.getValue().getOccupants()) {
                                final String uuid = occupant.getUuid();
                                boolean contains = false;
                                for (PubNubForJava client : clients) {
                                    if (client.getConfiguration().getUserId().getValue().equals(uuid)) {
                                        contains = true;
                                        break;
                                    }
                                }
                                assertTrue(contains);
                            }
                        }

                        success.set(true);
                    });
                });

        listen(success);
    }

    @Ignore("For now server doesn't emit state-change event on Heartbeat as default. To do this you need to set presence_heartbeat_state_change_event flag on keys. " +
            "Server plans to generate state-change event as default. Once server change is on Prod modify it by replacing Thread.sleep(10000) with Awaitility.await()")
    @Test
    public void should_setState_withHeartbeat() throws InterruptedException {
//        enableHeartbeatLoop(2);
        boolean WITH_HEARTBEAT_TRUE = true;
        final AtomicInteger hits = new AtomicInteger();
        final int expectedHits = 2;

        final JsonObject expectedStatePayload = generatePayload();
        final String expectedChannel = RandomGenerator.get();

        pubNub.addListener(new SubscribeCallback.BaseSubscribeCallback() {

            @Override
            public void presence(@NotNull PubNubForJava pubnub, @NotNull PNPresenceEventResult presence) {
                System.out.println("---" + presence.getEvent());
                if (presence.getEvent().equals(STATE_CHANGE_EVENT)
                        && presence.getChannel().equals(expectedChannel)
                        && presence.getUuid().equals(pubNub.getConfiguration().getUserId().getValue())) {
                    assertEquals(expectedStatePayload, presence.getState());
                    hits.incrementAndGet();
                }
            }
        });

        subscribeToChannel(pubNub, expectedChannel);

        pubNub.setPresenceState()
                .channels(Collections.singletonList(expectedChannel))
                .state(expectedStatePayload)
                .withHeartbeat(WITH_HEARTBEAT_TRUE)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertEquals(expectedStatePayload, result.getOrNull().getState());
                });

//        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAtomic(hits, IsEqual.equalTo(1));
        Thread.sleep(1000);

        pubNub.getPresenceState()
                .channels(Collections.singletonList(expectedChannel))
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertEquals(expectedStatePayload.get("text"), result.getOrNull().getStateByUUID().get(expectedChannel).getAsJsonObject().get("text"));
                    assertEquals(expectedStatePayload.get("info"), result.getOrNull().getStateByUUID().get(expectedChannel).getAsJsonObject().get("info"));
                    assertEquals(expectedStatePayload.get("uncd"), result.getOrNull().getStateByUUID().get(expectedChannel).getAsJsonObject().get("uncd"));
                    hits.incrementAndGet();
                });

//        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAtomic(hits, IsEqual.equalTo(expectedHits));
        Thread.sleep(1000);
    }

    @Test
    public void testPresenceState() {
        final AtomicInteger hits = new AtomicInteger();
        final int expectedHits = 2;

        final JsonObject expectedStatePayload = generatePayload();
        final String expectedChannel = RandomGenerator.get();

        pubNub.addListener(new EventListener() {
            @Override
            public void presence(@NotNull PubNubForJava pubnub, @NotNull PNPresenceEventResult presence) {
                if (presence.getEvent().equals(STATE_CHANGE_EVENT)
                        && presence.getChannel().equals(expectedChannel)
                        && presence.getUuid().equals(pubNub.getConfiguration().getUserId().getValue())) {
                    assertEquals(expectedStatePayload, presence.getState());
                    hits.incrementAndGet();
                }
            }
        });

        subscribeToChannel(pubNub, expectedChannel);

        pubNub.setPresenceState()
                .channels(Collections.singletonList(expectedChannel))
                .state(expectedStatePayload)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertEquals(expectedStatePayload, result.getOrNull().getState());
                });

        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAtomic(hits, IsEqual.equalTo(1));

        pubNub.getPresenceState()
                .channels(Collections.singletonList(expectedChannel))
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertEquals(expectedStatePayload, result.getOrNull().getStateByUUID().get(expectedChannel));
                    hits.incrementAndGet();
                });

        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAtomic(hits, IsEqual.equalTo(expectedHits));

    }

    @Test
    public void testHeartbeatsDisabled() {
        final AtomicInteger heartbeatCallsCount = new AtomicInteger(0);
        final AtomicBoolean subscribeSuccess = new AtomicBoolean();
        final String expectedChannel = RandomGenerator.get();

        pubNub = getPubNub(builder -> {
                    builder.heartbeatNotificationOptions(PNHeartbeatNotificationOptions.ALL);
                    builder.presenceTimeout(20);
                    builder.heartbeatInterval(0);
                });

        assertEquals(PNHeartbeatNotificationOptions.ALL, pubNub.getConfiguration().getHeartbeatNotificationOptions());
        assertEquals(20, pubNub.getConfiguration().getPresenceTimeout());
        assertEquals(0, pubNub.getConfiguration().getHeartbeatInterval());

        pubNub.addListener(new SubscribeCallback.BaseSubscribeCallback() {
            @Override
            public void status(@NotNull PubNubForJava pubnub, @NotNull PNStatus status) {
                if (!status.isError()) {
                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                        if (status.getAffectedChannels().contains(expectedChannel)) {
                            subscribeSuccess.set(true);
                        }
                    } else if (status.getCategory() == PNStatusCategory.PNHeartbeatSuccess) {
                        heartbeatCallsCount.incrementAndGet();
                    }
                }
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(expectedChannel))
                .withPresence()
                .execute();

        Awaitility.await()
                .atMost(22, TimeUnit.SECONDS)
                .pollDelay(21, TimeUnit.SECONDS)
                .until(() -> subscribeSuccess.get() && heartbeatCallsCount.get() == 0);
    }

    @Test
    public void testHeartbeatsEnabled() {
        final AtomicInteger heartbeatCallsCount = new AtomicInteger(0);
        final AtomicBoolean subscribeSuccess = new AtomicBoolean();
        final String expectedChannel = RandomGenerator.get();
        pubNub = getPubNub(builder -> {
            builder.presenceTimeout(20);
            builder.heartbeatNotificationOptions(PNHeartbeatNotificationOptions.ALL);
        });
        registerGuestClient(pubNub);
        assertEquals(9, pubNub.getConfiguration().getHeartbeatInterval());

        pubNub.addListener(new SubscribeCallback.BaseSubscribeCallback() {
            @Override
            public void status(@NotNull PubNubForJava pubnub, @NotNull PNStatus status) {
                if (!status.isError()) {
                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                        if (status.getAffectedChannels().contains(expectedChannel)) {
                            subscribeSuccess.set(true);
                        }
                    } else if (status.getCategory() == PNStatusCategory.PNHeartbeatSuccess) {
                        heartbeatCallsCount.incrementAndGet();
                    }
                }
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(expectedChannel))
                .withPresence()
                .execute();

        Awaitility.await()
                .atMost(20, TimeUnit.SECONDS)
                .until(() -> subscribeSuccess.get() && heartbeatCallsCount.get() > 2);
    }

}
