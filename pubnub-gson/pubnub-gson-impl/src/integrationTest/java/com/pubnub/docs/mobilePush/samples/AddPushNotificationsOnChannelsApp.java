package com.pubnub.docs.mobilePush.samples;

// snippet.addPushNotificationsOnChannelsApp

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNPushType;

import java.util.Arrays;

public class AddPushNotificationsOnChannelsApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());
        // snippet.hide
        com.pubnub.docs.support.LiveClients.trackCurrent(pubnub);
        // snippet.show

        // Add device to channels for push notifications
        pubnub.addPushNotificationsOnChannels()
                .pushType(PNPushType.FCM)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .deviceId("googleDevice")
                .async(result -> {
                    if (result.isSuccess()) {
                        System.out.println("Device added to channels for push notifications successfully.");
                    } else {
                        System.out.println("Failed to add device to channels.");
                    }
                });
    }
}
// snippet.end
