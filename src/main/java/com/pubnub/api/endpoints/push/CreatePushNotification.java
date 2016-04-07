package com.pubnub.api.endpoints.push;

import com.pubnub.api.core.PnCallback;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.PublishData;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class CreatePushNotification {

    private Pubnub pubnub;
    private PushType pushType;
    private Object pushPayload;
    private String channel;

    public PublishData sync() throws PubnubException {
        Map<String, Object> payload = preparePayload();
        return pubnub.publish().channel(channel).message(payload).build().sync();
    }

    public void async(final PnCallback<PublishData> callback) {
        Map<String, Object> payload = preparePayload();
        pubnub.publish().channel(channel).message(payload).build().async(callback);
    }

    private Map<String, Object> preparePayload() {
        Map<String, Object> payload = new HashMap<>();

        if (pushType == PushType.APNS) {
            payload.put("pn_apns", pushPayload);
        }

        if (pushType == PushType.GCM) {
            payload.put("pn_gcm", pushPayload);
        }

        if (pushType == PushType.MPNS) {
            payload.put("pn_mpns", pushPayload);
        }

        return payload;
    }


}
