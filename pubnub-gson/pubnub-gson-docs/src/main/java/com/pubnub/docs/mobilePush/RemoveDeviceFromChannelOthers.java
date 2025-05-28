package com.pubnub.docs.mobilePush;

import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.java.PubNub;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;

public class RemoveDeviceFromChannelOthers extends SnippetBase {
    private void removeDeviceFromChannel() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/mobile-push#remove-device-from-channel-1
        // removePushNotificationsFromChannels

        PubNub pubNub = createPubNub();

        // snippet.removeDeviceFromChannel
        pubNub.removePushNotificationsFromChannels()
                .deviceId("googleDevice")
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .pushType(PNPushType.FCM)
                .async(result -> { /* check result */ });
        // snippet.end
    }
}
