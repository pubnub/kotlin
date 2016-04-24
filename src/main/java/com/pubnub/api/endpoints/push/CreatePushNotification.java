package com.pubnub.api.endpoints.push;

import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.core.PubNub;
import com.pubnub.api.core.PubNubException;
import com.pubnub.api.core.enums.PushType;
import com.pubnub.api.core.models.consumer.PNPublishResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreatePushNotification {

    private PubNub pubnub;
    @Setter private PushType pushType;
    @Setter private Object pushPayload;
    @Setter private String channel;

    public CreatePushNotification(PubNub pubnub) {
        this.pubnub = pubnub;
    }

    public PNPublishResult sync() throws PubNubException {
        Map<String, Object> payload = preparePayload();
        return pubnub.publish().channel(channel).message(payload).sync();
    }

    public void async(final PNCallback<PNPublishResult> callback) {
        Map<String, Object> payload = preparePayload();
        pubnub.publish().channel(channel).message(payload).async(callback);
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
