package com.pubnub.docs.mobilePush;

// https://www.pubnub.com/docs/sdks/java/api-reference/mobile-push#add-device-to-channel-1

// snippet.addPushNotificationsOnChannelsApp

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNPushType;

import java.util.Arrays;

public class AddPushNotificationsOnChannelsApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

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
