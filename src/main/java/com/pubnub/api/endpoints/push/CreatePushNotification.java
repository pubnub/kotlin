package com.pubnub.api.endpoints.push;

import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PushType;
import com.pubnub.api.models.consumer.PNPublishResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreatePushNotification {

    private PubNub pubnub;
    @Setter private String channel;

    Map<String, Object> pushData;

    public CreatePushNotification(PubNub pubnub) {
        this.pubnub = pubnub;
        pushData = new HashMap<>();
    }

    public PNPublishResult sync() throws PubNubException {
        return pubnub.publish().channel(channel).message(pushData).sync();
    }

    public void async(final PNCallback<PNPublishResult> callback) {
        pubnub.publish().channel(channel).message(pushData).async(callback);
    }

    public CreatePushNotification addApplePayload(Map<String, Object> payload) {
        if (payload != null) {
            appendToPayload(PushType.APNS, payload);
        }
        return this;
    }

    public CreatePushNotification addGooglePayload(Map<String, Object> payload) {
        if (payload != null) {
            appendToPayload(PushType.GCM, payload);
        }
        return this;
    }

    public CreatePushNotification addMicrosoftPayload(Map<String, Object> payload) {
        if (payload != null) {
            appendToPayload(PushType.MPNS, payload);
        }
        return this;
    }

    public CreatePushNotification addPubNubPayload(Map<String, Object> payload) {
        if (payload != null) {
            for(String key : payload.keySet()) {
                pushData.put(key, payload.get(key));
            }
        }

        return this;
    }

    private void appendToPayload(PushType pushType, Map<String, Object> payload) {
        if (pushType == PushType.APNS) {
            pushData.put("pn_apns", payload);
        }

        if (pushType == PushType.GCM) {
            pushData.put("pn_gcm", payload);
        }

        if (pushType == PushType.MPNS) {
            pushData.put("pn_mpns", payload);
        }
    }

}
