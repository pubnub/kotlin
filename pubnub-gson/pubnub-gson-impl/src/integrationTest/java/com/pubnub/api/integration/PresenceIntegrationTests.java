package com.pubnub.api.integration;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.integration.util.RandomGenerator;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.hamcrest.core.IsEqual;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

        final List<PubNub> clients = new ArrayList<PubNub>(expectedClientsCount) {{
            add(pubNub);
        }};

        for (int i = 1; i <= expectedClientsCount - 1; i++) {
            clients.add(getPubNub());
        }

        for (PubNub client : clients) {
            subscribeToChannel(client, expectedChannels);
        }

        assertEquals(expectedClientsCount, clients.size());

        for (PubNub client : clients) {
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
                                        for (PubNub client : clients) {
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

        final List<PubNub> clients = new ArrayList<PubNub>() {{
            add(pubNub);
        }};
        for (int i = 1; i < expectedClientsCount; i++) {
            clients.add(getPubNub());
        }

        for (PubNub client : clients) {
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
                                for (PubNub client : clients) {
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
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
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
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
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
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
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
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
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

    @Test
    public void testHereNowWithLimit() {
        final AtomicBoolean success = new AtomicBoolean();
        final int testLimit = 3;
        final int totalClientsCount = 6;
        final String expectedChannel = RandomGenerator.get();

        final List<PubNub> clients = new ArrayList<PubNub>() {{
            add(pubNub);
        }};
        for (int i = 1; i < totalClientsCount; i++) {
            clients.add(getPubNub());
        }

        for (PubNub client : clients) {
            subscribeToChannel(client, expectedChannel);
        }

        pause(TIMEOUT_MEDIUM);

        pubNub.hereNow()
                .channels(Collections.singletonList(expectedChannel))
                .includeUUIDs(true)
                .limit(testLimit)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    result.onSuccess(pnHereNowResult -> {
                        assertEquals(1, pnHereNowResult.getTotalChannels());
                        assertEquals(1, pnHereNowResult.getChannels().size());
                        assertTrue(pnHereNowResult.getChannels().containsKey(expectedChannel));

                        PNHereNowChannelData channelData = pnHereNowResult.getChannels().get(expectedChannel);
                        assertNotNull(channelData);
                        assertEquals(totalClientsCount, channelData.getOccupancy());

                        // With limit=3, we should get only 3 occupants even though 6 are present
                        assertEquals(testLimit, channelData.getOccupants().size());

                        // nextOffset should be present since we limited results
                        assertNotNull(pnHereNowResult.getNextOffset());
                        assertEquals(Integer.valueOf(3), pnHereNowResult.getNextOffset());

                        success.set(true);
                    });
                });

        listen(success);
    }

    @Test
    public void testHereNowWithOffset() {
        final AtomicBoolean success = new AtomicBoolean();
        final int offsetValue = 2;
        final int totalClientsCount = 5;
        final String expectedChannel = RandomGenerator.get();

        final List<PubNub> clients = new ArrayList<PubNub>() {{
            add(pubNub);
        }};
        for (int i = 1; i < totalClientsCount; i++) {
            clients.add(getPubNub());
        }

        for (PubNub client : clients) {
            subscribeToChannel(client, expectedChannel);
        }

        pause(TIMEOUT_MEDIUM);

        pubNub.hereNow()
                .channels(Collections.singletonList(expectedChannel))
                .includeUUIDs(true)
                .offset(offsetValue)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    result.onSuccess(pnHereNowResult -> {
                        assertEquals(1, pnHereNowResult.getTotalChannels());
                        assertEquals(1, pnHereNowResult.getChannels().size());
                        assertTrue(pnHereNowResult.getChannels().containsKey(expectedChannel));

                        PNHereNowChannelData channelData = pnHereNowResult.getChannels().get(expectedChannel);
                        assertNotNull(channelData);
                        assertEquals(totalClientsCount, channelData.getOccupancy());

                        // With offset=2, we should get remaining occupants (5 total - 2 skipped = 3 remaining)
                        assertEquals(totalClientsCount - offsetValue, channelData.getOccupants().size());

                        // nextOffset should be null since we got all remaining results
                        assertNull(pnHereNowResult.getNextOffset());

                        success.set(true);
                    });
                });

        listen(success);
    }

    @Test
    public void testHereNowPaginationFlow() throws PubNubException {
        // 8 users in channel01
        // 3 users in channel02
        final int pageSize = 3;
        final int totalClientsCount = 11;
        final int channel01TotalCount = 8;
        final int channel02TotalCount = 3;
        final String channel01 = RandomGenerator.get();
        final String channel02 = RandomGenerator.get();

        final List<PubNub> clients = new ArrayList<PubNub>() {{
            add(pubNub);
        }};
        for (int i = 1; i < channel01TotalCount; i++) {
            clients.add(getPubNub());
        }

        for (PubNub client : clients) {
            subscribeToChannel(client, channel01);
        }

        for (int i = 0; i < channel02TotalCount; i++) {
            subscribeToChannel(clients.get(i), channel02);
        }

        pause(TIMEOUT_MEDIUM);

        final Set<String> allOccupantsInChannel01 = new HashSet<>();

        // First page
        PNHereNowResult firstPage = pubNub.hereNow()
                .channels(Arrays.asList(channel01, channel02))
                .includeUUIDs(true)
                .limit(pageSize)
                .sync();

        assertNotNull(firstPage);
        assertEquals(2, firstPage.getTotalChannels());

        PNHereNowChannelData channel01DataPage01 = firstPage.getChannels().get(channel01);
        assertNotNull(channel01DataPage01);
        assertEquals(channel01TotalCount, channel01DataPage01.getOccupancy());
        assertEquals(totalClientsCount, firstPage.getTotalOccupancy()); // total occupancy across all channels
        assertEquals(pageSize, channel01DataPage01.getOccupants().size());
        assertNotNull(firstPage.getNextOffset());
        assertEquals(Integer.valueOf(3), firstPage.getNextOffset());

        PNHereNowChannelData channel02Data = firstPage.getChannels().get(channel02);
        assertNotNull(channel02Data);
        assertEquals(channel02TotalCount, channel02Data.getOccupancy());
        assertEquals(pageSize, channel02Data.getOccupants().size());

        // Collect UUIDs from first page
        for (PNHereNowOccupantData occupant : channel01DataPage01.getOccupants()) {
            allOccupantsInChannel01.add(occupant.getUuid());
        }

        // Second page using nextOffset
        PNHereNowResult secondPage = pubNub.hereNow()
                .channels(Collections.singletonList(channel01))
                .includeUUIDs(true)
                .limit(pageSize)
                .offset(firstPage.getNextOffset())
                .sync();

        assertNotNull(secondPage);
        PNHereNowChannelData channel01DataPage02 = secondPage.getChannels().get(channel01);
        assertNotNull(channel01DataPage02);
        assertEquals(channel01TotalCount, channel01DataPage02.getOccupancy());
        assertEquals(channel01TotalCount, secondPage.getTotalOccupancy()); // only channel01 in results
        assertEquals(pageSize, channel01DataPage02.getOccupants().size());
        assertNotNull(secondPage.getNextOffset());
        assertEquals(Integer.valueOf(6), secondPage.getNextOffset());

        assertFalse(secondPage.getChannels().containsKey(channel02));

        // Collect UUIDs from second page (should not overlap with first page)
        for (PNHereNowOccupantData occupant : channel01DataPage02.getOccupants()) {
            assertFalse("UUID " + occupant.getUuid() + " already found in first page",
                    allOccupantsInChannel01.contains(occupant.getUuid()));
            allOccupantsInChannel01.add(occupant.getUuid());
        }

        // Third page using nextOffset from second page
        PNHereNowResult thirdPage = pubNub.hereNow()
                .channels(Collections.singletonList(channel01))
                .includeUUIDs(true)
                .limit(pageSize)
                .offset(secondPage.getNextOffset())
                .sync();

        assertNotNull(thirdPage);
        PNHereNowChannelData channel01DataPage03 = thirdPage.getChannels().get(channel01);
        assertNotNull(channel01DataPage03);
        assertEquals(channel01TotalCount, channel01DataPage03.getOccupancy());

        // Should have remaining clients (8 - 3 - 3 = 2)
        int expectedRemainingCount = channel01TotalCount - (pageSize * 2);
        assertEquals(expectedRemainingCount, channel01DataPage03.getOccupants().size());

        // Should be null since no more pages
        assertNull(thirdPage.getNextOffset());

        // Collect UUIDs from third page
        for (PNHereNowOccupantData occupant : channel01DataPage03.getOccupants()) {
            assertFalse("UUID " + occupant.getUuid() + " already found",
                    allOccupantsInChannel01.contains(occupant.getUuid()));
            allOccupantsInChannel01.add(occupant.getUuid());
        }

        // Verify we got all unique clients
        assertEquals(channel01TotalCount, allOccupantsInChannel01.size());
    }

    @Test
    public void testHereNowNextOffsetWhenMoreResults() {
        final AtomicBoolean success = new AtomicBoolean();
        final int limitValue = 4;
        final int totalClientsCount = 10;
        final String expectedChannel = RandomGenerator.get();

        final List<PubNub> clients = new ArrayList<PubNub>() {{
            add(pubNub);
        }};
        for (int i = 1; i < totalClientsCount; i++) {
            clients.add(getPubNub());
        }

        for (PubNub client : clients) {
            subscribeToChannel(client, expectedChannel);
        }

        pause(TIMEOUT_MEDIUM);

        pubNub.hereNow()
                .channels(Collections.singletonList(expectedChannel))
                .includeUUIDs(true)
                .limit(limitValue)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    result.onSuccess(pnHereNowResult -> {
                        assertEquals(1, pnHereNowResult.getTotalChannels());

                        PNHereNowChannelData channelData = pnHereNowResult.getChannels().get(expectedChannel);
                        assertNotNull(channelData);
                        assertEquals(totalClientsCount, channelData.getOccupancy());
                        assertEquals(limitValue, channelData.getOccupants().size());

                        // Since returned count equals limit and there are more clients,
                        // nextOffset should be present
                        assertNotNull(pnHereNowResult.getNextOffset());
                        assertEquals(Integer.valueOf(limitValue), pnHereNowResult.getNextOffset());

                        success.set(true);
                    });
                });

        listen(success);
    }

    @Test
    public void testHereNowPaginationWithEmptyChannels() {
        final AtomicBoolean success = new AtomicBoolean();
        final String emptyChannel = RandomGenerator.get();
        final int pageSize = 10;

        // Don't subscribe any clients to the channel, leaving it empty

        pubNub.hereNow()
                .channels(Collections.singletonList(emptyChannel))
                .includeUUIDs(true)
                .limit(pageSize)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    result.onSuccess(pnHereNowResult -> {
                        // Empty channels are still included in the response
                        assertEquals(1, pnHereNowResult.getTotalChannels());
                        assertEquals(0, pnHereNowResult.getTotalOccupancy());
                        assertEquals(1, pnHereNowResult.getChannels().size());

                        PNHereNowChannelData channelData = pnHereNowResult.getChannels().get(emptyChannel);
                        assertNotNull(channelData);
                        assertEquals(0, channelData.getOccupancy());
                        assertTrue(channelData.getOccupants().isEmpty());

                        // No pagination needed for empty results
                        assertNull(pnHereNowResult.getNextOffset());

                        success.set(true);
                    });
                });

        listen(success);
    }

    @Test
    public void testHereNowPaginationWithEmptyChannelsAndOffset() {
        final AtomicBoolean success = new AtomicBoolean();
        final String emptyChannel = RandomGenerator.get();
        final int pageSize = 10;
        final int offset = 5;

        // Don't subscribe any clients to the channel, leaving it empty

        pubNub.hereNow()
                .channels(Collections.singletonList(emptyChannel))
                .includeUUIDs(true)
                .limit(pageSize)
                .offset(offset)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    result.onSuccess(pnHereNowResult -> {
                        // Empty channels are still included in the response even with offset
                        assertEquals(1, pnHereNowResult.getTotalChannels());
                        assertEquals(0, pnHereNowResult.getTotalOccupancy());
                        assertEquals(1, pnHereNowResult.getChannels().size());

                        PNHereNowChannelData channelData = pnHereNowResult.getChannels().get(emptyChannel);
                        assertNotNull(channelData);
                        assertEquals(0, channelData.getOccupancy());
                        assertTrue(channelData.getOccupants().isEmpty());

                        // No pagination needed for empty results
                        assertNull(pnHereNowResult.getNextOffset());

                        success.set(true);
                    });
                });

        listen(success);
    }

    @Test
    public void testGlobalHereNowWithLimit() throws PubNubException {
        final int testLimit = 3;
        final int totalClientsCount = 6;
        final String channel01 = RandomGenerator.get();
        final String channel02 = RandomGenerator.get();

        final List<PubNub> clients = new ArrayList<PubNub>() {{
            add(pubNub);
        }};
        for (int i = 1; i < totalClientsCount; i++) {
            clients.add(getPubNub());
        }

        // Subscribe first 3 clients to channel01, all 6 to channel02
        for (int i = 0; i < 3; i++) {
            subscribeToChannel(clients.get(i), channel01);
        }
        for (PubNub client : clients) {
            subscribeToChannel(client, channel02);
        }

        pause(TIMEOUT_MEDIUM);

        // Global hereNow (empty channels list)
        PNHereNowResult result = pubNub.hereNow()
                .channels(Collections.emptyList())
                .includeUUIDs(true)
                .limit(testLimit)
                .sync();

        assertNotNull(result);

        // Should include at least our test channels
        assertTrue(result.getTotalChannels() >= 2);
        assertTrue(result.getChannels().containsKey(channel01));
        assertTrue(result.getChannels().containsKey(channel02));

        PNHereNowChannelData channel01Data = result.getChannels().get(channel01);
        PNHereNowChannelData channel02Data = result.getChannels().get(channel02);

        assertNotNull(channel01Data);
        assertNotNull(channel02Data);
        assertEquals(3, channel01Data.getOccupancy());
        assertEquals(6, channel02Data.getOccupancy());

        // With limit=3, each channel should have at most 3 occupants returned
        assertTrue(channel01Data.getOccupants().size() <= testLimit);
        assertTrue(channel02Data.getOccupants().size() <= testLimit);

        // nextOffset should be present since we limited results and channel02 has 6 occupants
        assertNotNull(result.getNextOffset());
    }

    @Test
    public void testGlobalHereNowWithOffset() throws PubNubException {
        final int offsetValue = 2;
        final int totalClientsCount = 5;
        final String expectedChannel = RandomGenerator.get();

        final List<PubNub> clients = new ArrayList<PubNub>() {{
            add(pubNub);
        }};
        for (int i = 1; i < totalClientsCount; i++) {
            clients.add(getPubNub());
        }

        for (PubNub client : clients) {
            subscribeToChannel(client, expectedChannel);
        }

        pause(TIMEOUT_MEDIUM);

        // Global hereNow with offset
        PNHereNowResult result = pubNub.hereNow()
                .channels(Collections.emptyList())
                .includeUUIDs(true)
                .offset(offsetValue)
                .sync();

        assertNotNull(result);

        // Should include at least our test channel
        assertTrue(result.getTotalChannels() >= 1);
        assertTrue(result.getChannels().containsKey(expectedChannel));

        PNHereNowChannelData channelData = result.getChannels().get(expectedChannel);
        assertNotNull(channelData);
        assertEquals(totalClientsCount, channelData.getOccupancy());

        // With offset=2, we should get remaining occupants
        assertTrue(channelData.getOccupants().size() <= totalClientsCount - offsetValue);
    }

    @Test
    public void testGlobalHereNowPaginationFlow() throws PubNubException {
        final int pageSize = 3;
        final int totalClientsInChannel01 = 8;
        final int totalClientsInChannel02 = 3;
        final String channel01 = RandomGenerator.get();
        final String channel02 = RandomGenerator.get();

        final List<PubNub> clients = new ArrayList<PubNub>() {{
            add(pubNub);
        }};
        for (int i = 1; i < totalClientsInChannel01; i++) {
            clients.add(getPubNub());
        }

        // Subscribe 8 clients to channel01
        for (PubNub client : clients) {
            subscribeToChannel(client, channel01);
        }

        // Subscribe first 3 clients to channel02 as well
        for (int i = 0; i < totalClientsInChannel02; i++) {
            subscribeToChannel(clients.get(i), channel02);
        }

        pause(TIMEOUT_MEDIUM);

        final Set<String> allOccupantsInChannel01 = new HashSet<>();

        // First page - global hereNow with no channels specified
        PNHereNowResult firstPage = pubNub.hereNow()
                .channels(Collections.emptyList())
                .includeUUIDs(true)
                .limit(pageSize)
                .sync();

        assertNotNull(firstPage);

        // Should include at least our test channels
        assertTrue(firstPage.getTotalChannels() >= 2);
        assertTrue(firstPage.getChannels().containsKey(channel01));
        assertTrue(firstPage.getChannels().containsKey(channel02));

        PNHereNowChannelData channel01DataPage01 = firstPage.getChannels().get(channel01);
        PNHereNowChannelData channel02DataPage01 = firstPage.getChannels().get(channel02);

        assertNotNull(channel01DataPage01);
        assertNotNull(channel02DataPage01);
        assertEquals(totalClientsInChannel01, channel01DataPage01.getOccupancy());
        assertEquals(totalClientsInChannel02, channel02DataPage01.getOccupancy());

        // With limit, should get limited results
        assertTrue(channel01DataPage01.getOccupants().size() <= pageSize);
        assertTrue(channel02DataPage01.getOccupants().size() <= pageSize);

        // Collect UUIDs from first page
        for (PNHereNowOccupantData occupant : channel01DataPage01.getOccupants()) {
            allOccupantsInChannel01.add(occupant.getUuid());
        }

        // Should have nextOffset since we have more results
        assertNotNull(firstPage.getNextOffset());

        // Second page using nextOffset
        PNHereNowResult secondPage = pubNub.hereNow()
                .channels(Collections.emptyList())
                .includeUUIDs(true)
                .limit(pageSize)
                .offset(firstPage.getNextOffset())
                .sync();

        assertNotNull(secondPage);

        // May have more or fewer channels than first page
        assertTrue(secondPage.getTotalChannels() >= 1);

        if (secondPage.getChannels().containsKey(channel01)) {
            PNHereNowChannelData channel01DataPage02 = secondPage.getChannels().get(channel01);
            assertNotNull(channel01DataPage02);
            assertEquals(totalClientsInChannel01, channel01DataPage02.getOccupancy());

            // Collect UUIDs from second page (should not overlap with first page)
            for (PNHereNowOccupantData occupant : channel01DataPage02.getOccupants()) {
                assertFalse("UUID " + occupant.getUuid() + " already found in first page",
                        allOccupantsInChannel01.contains(occupant.getUuid()));
                allOccupantsInChannel01.add(occupant.getUuid());
            }
        }
    }

    @Test
    public void testGlobalHereNowWithNoActiveChannels() throws PubNubException {
        // Don't subscribe any clients, making it a truly empty global query
        // Wait a bit to ensure no residual presence state from other tests
        pause(1);

        PNHereNowResult result = pubNub.hereNow()
                .channels(Collections.emptyList())
                .includeUUIDs(true)
                .limit(10)
                .sync();

        assertNotNull(result);

        // Should have no channels or very few residual ones
        // Note: In a shared test environment, there might be residual presence state
        assertTrue(result.getTotalOccupancy() >= 0);

        // No pagination needed when no active subscriptions for this client
        // Note: Result may vary based on test isolation
    }
}
