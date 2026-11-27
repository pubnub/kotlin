package com.pubnub.docs.presence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.api.models.consumer.presence.PNWhereNowResult;
import com.pubnub.api.v2.subscriptions.SubscriptionOptions;
import com.pubnub.docs.support.JavaWait;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.MessagingKit;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page presence (docs/sdks/java/api-reference/presence.md). One test per snippet. The text between
 * a start marker and {@code snippet.end} is the published sample, the arrangement is above it and the assertions below it. The
 * joiner is a second client (a Kotlin client named {@code docs-joiner}): the service lists a user as present only after it
 * subscribed.
 */
@ExtendWith(LiveExtension.class)
class PresenceDocsTest {
    private static final String OPEN = "open";

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    private static Map<String, Object> state(String key, Object value) {
        return Collections.singletonMap(key, value);
    }

    @Test
    void hereNow(LiveClients clients) throws Exception {
        MessagingKit.joinWithState(clients, "docs-joiner", Collections.singletonList("coolChannel"), state("mood", "happy"));
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.presence.samples.HereNowApp.main(new String[0]);

        // offset 10 skips the one occupant: the occupancy counts it, the list of occupants stays empty
        Wait.eventuallyRun("the sample printed the result", () -> {
            out.assertPrinted("Total Channels: 1");
            out.assertPrinted("Total Occupancy: 1");
            out.assertPrinted("Channel: coolChannel");
            out.assertPrinted("Occupancy: 1");
            out.assertPrinted("Occupants:");
        });
        assertTrue(out.lines("UUID: ").isEmpty(), "an occupant was listed although offset 10 skips it");
        assertTrue(out.lines("Error retrieving hereNow data").isEmpty(), "the sample reported an error");
    }

    @Test
    void hereNowForChannel(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(reader, "cg1", "ch1");
        Kit.createGroup(reader, "cg2", "ch2");
        Kit.createGroup(reader, "cg3", "ch3");
        MessagingKit.awaitGroup(reader, "cg1", "ch1");
        MessagingKit.joinWithState(clients, "docs-joiner", Collections.singletonList("ch1"), state("isTyping", true));
        Stdout out = Stdout.captureForTest();

        // snippet.hereNowForChannel
        pubNub.hereNow()
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3")) // who is present on channel groups?
                .includeState(true) // include state with request (false by default)
                .includeUUIDs(true) // if false, only shows occupancy count
                .async(result -> {
                    result.onSuccess((PNHereNowResult res) -> {
                        // Get total occupancy across all channels if the request was successful
                        System.out.println("Total Occupancy: " + res.getTotalOccupancy()); // 12

                        // Iterate through channels in the channel group
                        for (PNHereNowChannelData channelData : res.getChannels().values()) {
                            System.out.println("Channel: " + channelData.getChannelName());
                            System.out.println("Occupancy: " + channelData.getOccupancy());
                            System.out.println("Occupants:");
                            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                System.out.println("UUID: " + occupant.getUuid() + " State: " + occupant.getState());
                            }
                        }
                    }).onFailure((PubNubException exception) -> {
                        // Handle errors if the request fails
                        System.err.println("Error retrieving hereNow data: " + exception.getMessage());
                        exception.getPubnubError();
                        exception.getCause();
                        exception.getStatusCode();
                    });
                });
        // snippet.end

        PNHereNowResult here = (PNHereNowResult) recorder.awaitOk("hereNow");
        assertEquals(1, here.getTotalOccupancy());
        PNHereNowOccupantData occupant = here.getChannels().get("ch1").getOccupants().get(0);
        assertEquals("docs-joiner", occupant.getUuid());
        assertTrue(occupant.getState().getAsJsonObject().get("isTyping").getAsBoolean(), "the state is not listed");
        Wait.eventuallyRun("the sample printed the occupant", () -> {
            out.assertPrinted("Total Occupancy: 1");
            out.assertPrinted("UUID: docs-joiner State: {\"isTyping\":true}");
        });
    }

    @Test
    void hereNowOccupancyOnly(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        MessagingKit.joinWithState(clients, "docs-joiner", Collections.singletonList("my_channel"), state("isTyping", true));
        Stdout out = Stdout.captureForTest();

        // snippet.hereNowOccupancyOnly
        pubNub.hereNow()
                .channels(Arrays.asList("my_channel")) // who is present on those channels?
                .includeState(false) // include state with request (false by default)
                .includeUUIDs(false) // if false, only shows occupancy count
                .async(result -> {
                    result.onSuccess((PNHereNowResult res) -> {
                        // Iterate through channels if the request was successful
                        for (PNHereNowChannelData channelData : res.getChannels().values()) {
                            System.out.println("Channel: " + channelData.getChannelName()); // my_channel
                            System.out.println("Occupancy: " + channelData.getOccupancy()); // 3
                        }
                    }).onFailure((PubNubException exception) -> {
                        // Handle errors if the request fails
                        System.err.println("Error retrieving hereNow data: " + exception.getMessage());
                        exception.getPubnubError();
                        exception.getCause();
                        exception.getStatusCode();
                    });
                });
        // snippet.end

        PNHereNowResult here = (PNHereNowResult) recorder.awaitOk("hereNow");
        PNHereNowChannelData channelData = here.getChannels().get("my_channel");
        assertEquals(1, channelData.getOccupancy());
        assertTrue(channelData.getOccupants().isEmpty(), "the occupants are listed although includeUUIDs is false");
        Wait.eventuallyRun("the sample printed the occupancy", () -> {
            out.assertPrinted("Channel: my_channel");
            out.assertPrinted("Occupancy: 1");
        });
    }

    @Test
    void hereNowReturningState(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        MessagingKit.joinWithState(clients, "docs-joiner", Collections.singletonList("my_channel"), state("isTyping", true));
        Stdout out = Stdout.captureForTest();

        // snippet.hereNowReturningState
        pubNub.hereNow()
                .channels(Arrays.asList("my_channel")) // who is present on those channels?
                .includeState(true) // include state with request (false by default)
                .includeUUIDs(true) // if false, only shows occupancy count
                .async(result -> {
                    result.onSuccess((PNHereNowResult res) -> {
                        // Iterate through channels if the request was successful
                        for (PNHereNowChannelData channelData : res.getChannels().values()) {
                            System.out.println("Channel: " + channelData.getChannelName());
                            System.out.println("Occupancy: " + channelData.getOccupancy());
                            System.out.println("Occupants:");
                            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                System.out.println("UUID: " + occupant.getUuid() + " State: " + occupant.getState());
                            }
                        }
                    }).onFailure((PubNubException exception) -> {
                        // Handle errors if the request fails
                        System.err.println("Error retrieving hereNow data: " + exception.getMessage());
                        exception.getPubnubError();
                        exception.getCause();
                        exception.getStatusCode();
                    });
                });
        // snippet.end

        PNHereNowResult here = (PNHereNowResult) recorder.awaitOk("hereNow");
        PNHereNowOccupantData occupant = here.getChannels().get("my_channel").getOccupants().get(0);
        assertEquals("docs-joiner", occupant.getUuid());
        assertTrue(occupant.getState().getAsJsonObject().get("isTyping").getAsBoolean(), "the state is not listed");
        Wait.eventuallyRun("the sample printed the occupant with its state", () -> {
            out.assertPrinted("Occupancy: 1");
            out.assertPrinted("UUID: docs-joiner State: {\"isTyping\":true}");
        });
    }

    @Test
    void getState(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(reader, "cg1", "ch4");
        Kit.createGroup(reader, "cg2", "ch5");
        Kit.createGroup(reader, "cg3", "ch6");
        com.pubnub.api.PubNub owner = kotlinClient(clients, "suchUUID");
        owner.setPresenceState(Arrays.asList("ch1", "ch2", "ch3"), Collections.emptyList(), state("is_typing", true)).sync();

        // snippet.getState
        pubNub.getPresenceState()
                .channels(Arrays.asList("ch1", "ch2", "ch3")) // channels to fetch state for
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3")) // channel groups to fetch state for
                .uuid("suchUUID") // uuid of user to fetch, or for own uuid
                .async(result -> { /* check result */ });
        // snippet.end

        PNGetStateResult read = (PNGetStateResult) recorder.awaitOk("getPresenceState");
        // several channels come back under the one key "channels"
        JsonObject byChannel = read.getStateByUUID().get("channels").getAsJsonObject();
        for (String channel : Arrays.asList("ch1", "ch2", "ch3")) {
            assertTrue(byChannel.get(channel).getAsJsonObject().get("is_typing").getAsBoolean(), "the state is missing on " + channel);
        }
    }

    @Test
    void setState(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");

        // snippet.setState
        String channel = "my_channel";
        JsonObject state = new JsonObject();
        state.addProperty("is_typing", true);

        pubNub.setPresenceState()
                .channels(Arrays.asList(channel))
                .state(state)
                .async(result -> { /* check result */ });
        // snippet.end

        PNSetStateResult set = (PNSetStateResult) recorder.awaitOk("setPresenceState");
        assertTrue(set.getState().getAsJsonObject().get("is_typing").getAsBoolean(), "the answer does not carry the state");
        Kit.eventually("the state of yourUserId is stored", () -> {
            PNGetStateResult read = reader.getPresenceState(Arrays.asList("my_channel"), Collections.emptyList(), "yourUserId").sync();
            assertTrue(read.getStateByUUID().get("my_channel").getAsJsonObject().get("is_typing").getAsBoolean(), "the state is not stored");
        });
    }

    @Test
    void setStateForChannelsInChannelGroup(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(reader, "cg1", "ch4");
        Kit.createGroup(reader, "cg2", "ch5");
        Kit.createGroup(reader, "cg3", "ch6");

        // snippet.setStateForChannelsInChannelGroup
        JsonObject state = new JsonObject();
        state.addProperty("is_typing", true);

        pubNub.setPresenceState()
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3")) // apply on those channel groups
                .channels(Arrays.asList("ch1", "ch2", "ch3")) // apply on those channels
                .state(state) // the new state
                .async(result -> { /* check result */ });
        // snippet.end

        PNSetStateResult set = (PNSetStateResult) recorder.awaitOk("setPresenceState");
        assertTrue(set.getState().getAsJsonObject().get("is_typing").getAsBoolean(), "the answer does not carry the state");
        Kit.eventually("the state of yourUserId is stored on the channels", () -> {
            PNGetStateResult read = reader.getPresenceState(Arrays.asList("ch1", "ch2", "ch3"), Collections.emptyList(), "yourUserId").sync();
            JsonObject byChannel = read.getStateByUUID().get("channels").getAsJsonObject();
            for (String channel : Arrays.asList("ch1", "ch2", "ch3")) {
                assertTrue(byChannel.get(channel).getAsJsonObject().get("is_typing").getAsBoolean(), "the state is missing on " + channel);
            }
        });
    }

    @Test
    void setStateResponseOnListener(LiveClients clients) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = client;
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.awaitNoOccupants(reader, "ch1");

        // snippet.setStateResponseOnListener
        Channel channel = pubNub.channel("ch1");
        Subscription subscription = channel.subscription(SubscriptionOptions.receivePresenceEvents());

        subscription.setOnPresence(pnPresenceEventResult -> {
            if (pnPresenceEventResult.getEvent().equals("state-change")) {
                boolean is_typing = pnPresenceEventResult.getState()
                        .getAsJsonObject()
                        .get("is_typing")
                        .getAsBoolean();
            }
        });
        // snippet.end

        // The sample only builds the subscription and its listener. The test subscribes it, lets another user change its state
        // and reads the same field the listener reads from the event the client delivers.
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("ch1"), Collections.emptyList());
        subscription.subscribe();
        connected.await();
        com.pubnub.api.PubNub joiner = kotlinClient(clients, "docs-joiner");
        Kit.subscribeAndConnect(joiner, Collections.singletonList("ch1"));
        JavaWait.Expectation changed = JavaWait.presence(client, "state-change", "docs-joiner", "ch1");
        joiner.setPresenceState(Collections.singletonList("ch1"), Collections.emptyList(), state("is_typing", true)).sync();
        PNPresenceEventResult event = (PNPresenceEventResult) changed.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        assertEquals("state-change", event.getEvent());
        assertTrue(event.getState().getAsJsonObject().get("is_typing").getAsBoolean(), "the event does not carry the state");
    }

    @Test
    void getListOfChannelsOtherUserIsSubscribeTo(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.awaitNoOccupants(reader, "ch1");
        com.pubnub.api.PubNub other = kotlinClient(clients, "some-other-uuid");
        Kit.subscribeAndConnect(other, Collections.singletonList("ch1"));
        MessagingKit.awaitPresent(reader, "ch1", "some-other-uuid");
        Wait.settledRun("the service lists ch1 for some-other-uuid", () -> {
            try {
                assertEquals(Collections.singletonList("ch1"), reader.whereNow("some-other-uuid").sync().getChannels());
            } catch (PubNubException e) {
                throw new AssertionError("whereNow failed", e);
            }
        });
        Stdout out = Stdout.captureForTest();

        // snippet.getListOfChannelsOtherUserIsSubscribeTo
        pubNub.whereNow().uuid("some-other-uuid").async(result -> {
            result.onSuccess((PNWhereNowResult res) -> {
                System.out.println("some-other-uuid is currently subscribed to following channels: " + res.getChannels());
            }).onFailure((PubNubException exception) -> {
                // Handle errors if the request fails
                System.err.println("Error retrieving whereNow data: " + exception.getMessage());
                exception.getPubnubError();
                exception.getCause();
                exception.getStatusCode();
            });
        });
        // snippet.end

        PNWhereNowResult where = (PNWhereNowResult) recorder.awaitOk("whereNow");
        assertEquals(Collections.singletonList("ch1"), where.getChannels());
        Wait.eventuallyRun("the sample printed the channels", () -> out.assertPrinted("some-other-uuid is currently subscribed to following channels: [ch1]"));
    }

    @Test
    void getListOfChannelsUuidIsSubscribeTo(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.awaitNoOccupants(reader, "ch1", "ch2");
        JavaWait.Expectation connected = JavaWait.connected(client, Arrays.asList("ch1", "ch2"), Collections.emptyList());
        client.subscribe().channels(Arrays.asList("ch1", "ch2")).execute();
        connected.await();
        MessagingKit.awaitPresent(reader, "ch1", "yourUserId");
        MessagingKit.awaitPresent(reader, "ch2", "yourUserId");
        Wait.settledRun("the service lists both channels for yourUserId", () -> {
            try {
                List<String> channels = new ArrayList<>(reader.whereNow("yourUserId").sync().getChannels());
                Collections.sort(channels);
                assertEquals(Arrays.asList("ch1", "ch2"), channels);
            } catch (PubNubException e) {
                throw new AssertionError("whereNow failed", e);
            }
        });
        Stdout out = Stdout.captureForTest();

        // snippet.getListOfChannelsUuidIsSubscribeTo
        pubNub.whereNow().async(result -> {
            result.onSuccess((PNWhereNowResult res) -> {
                System.out.println("User is currently subscribed to following channels: " + res.getChannels());
            }).onFailure((PubNubException exception) -> {
                // Handle errors if the request fails
                System.err.println("Error retrieving whereNow data: " + exception.getMessage());
                exception.getPubnubError();
                exception.getCause();
                exception.getStatusCode();
            });
        });
        // snippet.end

        PNWhereNowResult where = (PNWhereNowResult) recorder.awaitOk("whereNow");
        List<String> channels = new ArrayList<>(where.getChannels());
        Collections.sort(channels);
        assertEquals(Arrays.asList("ch1", "ch2"), channels);
        Wait.eventuallyRun("the sample printed the channels", () -> out.assertPrinted("User is currently subscribed to following channels: ["));
    }
}
