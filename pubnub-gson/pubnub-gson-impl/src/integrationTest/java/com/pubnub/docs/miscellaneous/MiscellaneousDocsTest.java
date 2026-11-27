package com.pubnub.docs.miscellaneous;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.util.TimetokenUtil;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.JavaWait;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page misc (docs/sdks/java/api-reference/misc.md). One test per snippet. The text between a start
 * marker and {@code snippet.end} is the published sample, the arrangement is above it and the assertions below it. The
 * encryption samples that {@code MiscellaneousOthers} also holds are tested with the encryption page.
 */
@ExtendWith(LiveExtension.class)
class MiscellaneousDocsTest {
    private static final String OPEN = "open";

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Create push payload
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void createPushPayloadApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-config-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "foo", "pn_fcm");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("foo"));
        Cleanup.record("history", "foo");
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.miscellaneous.samples.CreatePushPayloadApp.main(new String[0]);

        // the sample publishes the payload it built, the callback of the sample prints the result
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals("demoUserId", message.getPublisher());
        Kit.assertTimetoken(message.getTimetoken());
        com.google.gson.JsonObject body = message.getMessage().getAsJsonObject();
        // the common payload that native subscribers get next to the push parts
        assertEquals("Hello", body.get("message").getAsString());
        assertEquals("object", body.get("such").getAsString());
        assertEquals(7, body.get("type").getAsInt());
        assertEquals("Notification title", body.getAsJsonObject("pn_fcm").getAsJsonObject("notification").get("title").getAsString());
        assertEquals("Alert", body.getAsJsonObject("pn_apns").getAsJsonObject("aps").get("alert").getAsString());
        Wait.eventuallyRun("the sample printed the result", () -> out.assertPrinted("Message published successfully."));
        Kit.storedItem(receiver, "foo", message.getTimetoken());
    }

    @Test
    void createPushPayloadBasic(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-config-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "foo", "pn_fcm");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("foo"));
        Cleanup.record("history", "foo");

        // snippet.createPushPayloadBasic
        PushPayloadHelper helper = new PushPayloadHelper();
        helper.setApnsPayload(new PushPayloadHelper.APNSPayload());
        helper.setFcmPayloadV2(new PushPayloadHelper.FCMPayloadV2());
        helper.setCommonPayload(new HashMap<String, Object>()); // or your actual payload

        Map<String, Object> payload = helper.build();
        // snippet.end

        // the docs: build() returns a Map that goes straight into publish as the message
        // (the empty parts of the sample build to nothing, so the parts get content before the build)
        PushPayloadHelper.FCMPayloadV2 fcm = new PushPayloadHelper.FCMPayloadV2();
        PushPayloadHelper.FCMPayloadV2.Notification notification = new PushPayloadHelper.FCMPayloadV2.Notification();
        notification.setTitle("Title");
        fcm.setNotification(notification);
        helper.setFcmPayloadV2(fcm);
        PushPayloadHelper.APNSPayload apns = new PushPayloadHelper.APNSPayload();
        PushPayloadHelper.APNSPayload.APS aps = new PushPayloadHelper.APNSPayload.APS();
        aps.setAlert("Alert");
        apns.setAps(aps);
        helper.setApnsPayload(apns);
        payload = helper.build();
        assertTrue(payload.containsKey("pn_fcm"), "the payload has no pn_fcm part");
        assertTrue(payload.containsKey("pn_apns"), "the payload has no pn_apns part");
        PNPublishResult published = javaClient(clients, "docs-config-sender").publish().channel("foo").message(payload).sync();
        PNMessageResult received = (PNMessageResult) arrived.await();
        assertEquals(published.getTimetoken(), received.getTimetoken());
        assertTrue(received.getMessage().getAsJsonObject().has("pn_fcm"));
        assertTrue(received.getMessage().getAsJsonObject().has("pn_apns"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Destroy, subscribed names, disconnect, reconnect
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void destroyPubNubBasic(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub watcher = kotlinClient(clients, "docs-config-receiver");
        Kit.awaitNoOccupants(watcher, "docs-misc-destroy");
        Wait.Expectation joined = Kit.armPresence(watcher, "join", "docs-config-sender", "docs-misc-destroy");
        Kit.subscribeAndConnect(watcher, Collections.singletonList("docs-misc-destroy"), Collections.emptyList(), true);
        PubNub client = javaClient(clients, "docs-config-sender");
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("docs-misc-destroy"), Collections.emptyList());
        client.subscribe().channels(Collections.singletonList("docs-misc-destroy")).execute();
        connected.await();
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        String text = Kit.probe("after-destroy");
        JavaWait.Expectation seenByClient = JavaWait.message(client, "docs-misc-destroy", text);
        Wait.Expectation seenByWatcher = Kit.armMessage(watcher, "docs-misc-destroy", text);
        PubNub pubNub = recorder.wrap(client, PubNub.class);

        // snippet.destroyPubNubBasic
        pubNub.destroy();
        // snippet.end

        // destroy frees the threads: the client is done and hears nothing, a watcher proves the service delivers
        javaClient(clients, "docs-config-writer").publish().channel("docs-misc-destroy").message(text).sync();
        seenByWatcher.await();
        JavaWait.assertNothingArrives(seenByClient, "a destroyed client received a message", 3000);
    }

    @Test
    void getSubscribedChannelGroupsBasic(LiveClients clients) throws Exception {
        PubNub pubNub = javaClient(clients, "docs-config-sender");
        com.pubnub.api.PubNub helper = kotlinClient(clients, "docs-config-receiver");
        Kit.createGroup(helper, "docs-misc-group", "docs-misc-ch1");
        JavaWait.Expectation connected = JavaWait.connected(pubNub, Collections.emptyList(), Collections.singletonList("docs-misc-group"));
        pubNub.subscribe().channelGroups(Collections.singletonList("docs-misc-group")).execute();
        connected.await();

        // snippet.getSubscribedChannelGroupsBasic
        List<String> groups = pubNub.getSubscribedChannelGroups();
        // snippet.end

        assertEquals(Collections.singletonList("docs-misc-group"), groups);
    }

    @Test
    void getSubscribedChannelsBasic(LiveClients clients) throws Exception {
        PubNub pubNub = javaClient(clients, "docs-config-sender");
        JavaWait.Expectation connected = JavaWait.connected(pubNub, Arrays.asList("docs-misc-ch1", "docs-misc-ch2"), Collections.emptyList());
        pubNub.subscribe().channels(Arrays.asList("docs-misc-ch1", "docs-misc-ch2")).execute();
        connected.await();

        // snippet.getSubscribedChannelsBasic
        List<String> channels = pubNub.getSubscribedChannels();
        // snippet.end

        assertEquals(2, channels.size());
        assertEquals(new java.util.HashSet<>(Arrays.asList("docs-misc-ch1", "docs-misc-ch2")), new java.util.HashSet<>(channels));
    }

    @Test
    void disconnectBasic(LiveClients clients) throws Exception {
        PubNub pubNub = javaClient(clients, "docs-config-sender");
        PubNub publisher = javaClient(clients, "docs-config-receiver");
        com.pubnub.api.PubNub control = kotlinClient(clients, "docs-misc-control");
        String before = Kit.probe("before-disconnect");
        String after = Kit.probe("after-disconnect");
        JavaWait.Expectation connected = JavaWait.connected(pubNub, Collections.singletonList("docs-misc-ch1"), Collections.emptyList());
        JavaWait.Expectation seenBefore = JavaWait.message(pubNub, "docs-misc-ch1", before);
        JavaWait.Expectation seenAfter = JavaWait.message(pubNub, "docs-misc-ch1", after);
        Wait.Expectation controlSeesAfter = Kit.armMessage(control, "docs-misc-ch1", after);
        pubNub.subscribe().channels(Collections.singletonList("docs-misc-ch1")).execute();
        connected.await();
        Kit.subscribeAndConnect(control, Collections.singletonList("docs-misc-ch1"));
        Cleanup.record("history", "docs-misc-ch1");
        publisher.publish().channel("docs-misc-ch1").message(before).sync();
        seenBefore.await();

        // snippet.disconnectBasic
        pubNub.disconnect();
        // snippet.end

        // a control client proves the service delivers while the disconnected client hears nothing
        publisher.publish().channel("docs-misc-ch1").message(after).sync();
        controlSeesAfter.await();
        JavaWait.assertNothingArrives(seenAfter, "a disconnected client received a message", 3000);
        // the client keeps its subscriptions, only the connection is stopped
        assertEquals(Collections.singletonList("docs-misc-ch1"), pubNub.getSubscribedChannels());
    }

    @Test
    void reconnectBasic(LiveClients clients) throws Exception {
        PubNub pubNub = javaClient(clients, "docs-config-sender");
        PubNub publisher = javaClient(clients, "docs-config-receiver");
        JavaWait.Expectation connected = JavaWait.connected(pubNub, Collections.singletonList("docs-misc-ch1"), Collections.emptyList());
        pubNub.subscribe().channels(Collections.singletonList("docs-misc-ch1")).execute();
        connected.await();
        Cleanup.record("history", "docs-misc-ch1");
        pubNub.disconnect();

        // snippet.reconnectBasic
        pubNub.reconnect();
        // or
        Long timetoken = 17276954606232118L; // Example timetoken received in publish/signal response
        pubNub.reconnect(timetoken);
        // snippet.end

        // the subscribe loop runs again and takes a new message
        String later = Kit.probe("after-reconnect");
        JavaWait.Expectation seenLater = JavaWait.message(pubNub, "docs-misc-ch1", later);
        PNPublishResult published = publisher.publish().channel("docs-misc-ch1").message(later).sync();
        PNMessageResult received = (PNMessageResult) seenLater.await();
        assertEquals(published.getTimetoken(), received.getTimetoken());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Timetoken conversions. The docs print the same lines for these samples.
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void timetokenToDateTimeBasic() {
        Stdout out = Stdout.captureForTest();

        // snippet.timetokenToDateTimeBasic
        long timetoken = 17276954606232118L;
        Instant instant = TimetokenUtil.timetokenToInstant(timetoken);
        LocalDateTime localDateTimeInUTC = LocalDateTime.ofInstant(instant, java.time.ZoneId.of("UTC"));

        System.out.println("PubNub timetoken: " + timetoken);
        System.out.println("Current date: " + localDateTimeInUTC.toLocalDate());
        System.out.println("Current time: " + localDateTimeInUTC.toLocalTime());
        // snippet.end

        out.assertPrinted("PubNub timetoken: 17276954606232118");
        out.assertPrinted("Current date: 2024-09-30");
        out.assertPrinted("Current time: 11:24:20.623211800");
        assertEquals(1727695460623L, instant.toEpochMilli());
        assertEquals(17276954606232118L, TimetokenUtil.instantToTimetoken(instant));
    }

    @Test
    void dateTimeToTimetokenBasic() {
        Stdout out = Stdout.captureForTest();

        // snippet.dateTimeToTimetokenBasic
        LocalDateTime localDateTime = LocalDateTime.of(2024, 9, 30, 12, 12, 44, 123456789); // Example date and time
        Instant instant = localDateTime.atOffset(java.time.ZoneOffset.UTC).toInstant();
        long timetoken = TimetokenUtil.instantToTimetoken(instant);

        System.out.println("Current date: " + localDateTime.toLocalDate());
        System.out.println("Current time: " + localDateTime.toLocalTime());
        System.out.println("PubNub timetoken: " + timetoken);
        // snippet.end

        out.assertPrinted("Current date: 2024-09-30");
        out.assertPrinted("Current time: 12:12:44.123456789");
        out.assertPrinted("PubNub timetoken: 17276983641234567");
        assertEquals(17276983641234567L, timetoken);
    }

    @Test
    void unixTimeToTimetokenBasic() {
        Stdout out = Stdout.captureForTest();

        // snippet.unixTimeToTimetokenBasic
        long unixTime = 1727866935316L;
        long timetoken = TimetokenUtil.unixToTimetoken(unixTime);
        Instant instant = TimetokenUtil.timetokenToInstant(timetoken);
        LocalDateTime localDateTimeInUTC = LocalDateTime.ofInstant(instant, java.time.ZoneId.of("UTC"));

        System.out.println("PubNub timetoken: " + timetoken);
        System.out.println("Current date: " + localDateTimeInUTC.toLocalDate());
        System.out.println("Current time: " + localDateTimeInUTC.toLocalTime());
        // snippet.end

        // the docs: 2024-10-02 11:02:15.316. The input is in milliseconds
        out.assertPrinted("PubNub timetoken: 17278669353160000");
        out.assertPrinted("Current date: 2024-10-02");
        out.assertPrinted("Current time: 11:02:15.316");
        assertEquals(17278669353160000L, timetoken);
    }

    @Test
    void timetokenToUnixTimeBasic() {
        Stdout out = Stdout.captureForTest();

        // snippet.timetokenToUnixTimeBasic
        long timetoken = 17276954606232118L;
        long unixTime = TimetokenUtil.timetokenToUnix(timetoken);
        Instant instant = Instant.ofEpochMilli(unixTime);
        LocalDateTime localDateTime = instant.atZone(java.time.ZoneOffset.UTC).toLocalDateTime();

        System.out.println("Current date: " + localDateTime.toLocalDate());
        System.out.println("Current time: " + localDateTime.toLocalTime());
        System.out.println("PubNub timetoken: " + timetoken);
        // snippet.end

        // the docs: 2024-09-30 11:24:20.623
        out.assertPrinted("Current date: 2024-09-30");
        out.assertPrinted("Current time: 11:24:20.623");
        out.assertPrinted("PubNub timetoken: 17276954606232118");
        assertEquals(1727695460623L, unixTime);
    }
}
