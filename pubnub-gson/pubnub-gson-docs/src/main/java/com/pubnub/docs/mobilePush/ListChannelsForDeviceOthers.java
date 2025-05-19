package com.pubnub.docs.mobilePush;

import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.java.PubNub;
import com.pubnub.docs.SnippetBase;

public class ListChannelsForDeviceOthers extends SnippetBase {
    private void auditPushChannelProvisionsBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/mobile-push#list-channels-for-device-1

        PubNub pubNub = createPubNub();

        // snippet.listChannelsForDevice
        pubNub.auditPushChannelProvisions()
                .deviceId("googleDevice")
                .pushType(PNPushType.FCM)
                .async(result -> { /* check result */ });
        // snippet.end
    }
}
