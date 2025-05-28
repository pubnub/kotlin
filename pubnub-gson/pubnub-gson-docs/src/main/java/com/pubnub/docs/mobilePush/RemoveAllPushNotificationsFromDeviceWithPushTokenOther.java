package com.pubnub.docs.mobilePush;

import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.java.PubNub;
import com.pubnub.docs.SnippetBase;

public class RemoveAllPushNotificationsFromDeviceWithPushTokenOther extends SnippetBase {
    private void removeAllPushNotificationsFromDeviceWithPushToken() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/mobile-push#remove-all-mobile-push-notifications-1

        PubNub pubNub;
        pubNub = createPubNub();

        // snippet.removeAllPushNotificationsFromDeviceWithPushToken
        pubNub.removeAllPushNotificationsFromDeviceWithPushToken()
                .deviceId("googleDevice")
                .pushType(PNPushType.FCM)
                .async(result -> { /* check result */ });
        // snippet.end
    }
}
