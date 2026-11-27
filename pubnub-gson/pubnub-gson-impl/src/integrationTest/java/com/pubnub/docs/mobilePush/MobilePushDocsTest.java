package com.pubnub.docs.mobilePush;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page mobile-push (docs/sdks/java/api-reference/mobile-push.md). One test per snippet. The text
 * between a start marker and {@code snippet.end} is the published sample, the arrangement is above it and the assertions below
 * it. The samples register a device token, they do not send a push. The Java samples use the FCM gateway only, which accepts
 * any token string, so the token of the docs ({@code googleDevice}) goes to the service as written.
 */
@ExtendWith(LiveExtension.class)
class MobilePushDocsTest {
    private static final String OPEN = "open";
    private static final String DEVICE = "googleDevice";

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    @Test
    void addPushNotificationsOnChannelsApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.seedPush(reader, PNPushType.FCM, DEVICE);
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.mobilePush.samples.AddPushNotificationsOnChannelsApp.main(new String[0]);

        Wait.eventuallyRun("the sample printed the success line", () -> out.assertPrinted("Device added to channels for push notifications successfully."));
        MessagingKit.awaitPush(reader, PNPushType.FCM, DEVICE, "ch1", "ch2", "ch3");
    }

    @Test
    void listChannelsForDevice(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.seedPush(reader, PNPushType.FCM, DEVICE, "ch1", "ch2");

        // snippet.listChannelsForDevice
        pubNub.auditPushChannelProvisions()
                .deviceId("googleDevice")
                .pushType(PNPushType.FCM)
                .async(result -> { /* check result */ });
        // snippet.end

        PNPushListProvisionsResult listed = (PNPushListProvisionsResult) recorder.awaitOk("auditPushChannelProvisions");
        List<String> channels = new ArrayList<>(listed.getChannels());
        Collections.sort(channels);
        assertEquals(Arrays.asList("ch1", "ch2"), channels);
    }

    @Test
    void removeAllPushNotificationsFromDeviceWithPushToken(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.seedPush(reader, PNPushType.FCM, DEVICE, "ch1", "ch2");

        // snippet.removeAllPushNotificationsFromDeviceWithPushToken
        pubNub.removeAllPushNotificationsFromDeviceWithPushToken()
                .deviceId("googleDevice")
                .pushType(PNPushType.FCM)
                .async(result -> { /* check result */ });
        // snippet.end

        recorder.awaitOk("removeAllPushNotificationsFromDeviceWithPushToken");
        MessagingKit.awaitPush(reader, PNPushType.FCM, DEVICE);
    }

    @Test
    void removeDeviceFromChannel(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.seedPush(reader, PNPushType.FCM, DEVICE, "ch1", "ch2", "ch3", "ch4");

        // snippet.removeDeviceFromChannel
        pubNub.removePushNotificationsFromChannels()
                .deviceId("googleDevice")
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .pushType(PNPushType.FCM)
                .async(result -> { /* check result */ });
        // snippet.end

        recorder.awaitOk("removePushNotificationsFromChannels");
        MessagingKit.awaitPush(reader, PNPushType.FCM, DEVICE, "ch4");
    }
}
