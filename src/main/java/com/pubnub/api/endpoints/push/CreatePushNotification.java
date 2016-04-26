package com.pubnub.api.endpoints.push;

import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNPushType;
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
            appendToPayload(PNPushType.APNS, payload);
        }
        return this;
    }

    public CreatePushNotification addGooglePayload(Map<String, Object> payload) {
        if (payload != null) {
            appendToPayload(PNPushType.GCM, payload);
        }
        return this;
    }

    public CreatePushNotification addMicrosoftPayload(Map<String, Object> payload) {
        if (payload != null) {
            appendToPayload(PNPushType.MPNS, payload);
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

    private void appendToPayload(PNPushType pushType, Map<String, Object> payload) {
        if (pushType == PNPushType.APNS) {
            pushData.put("pn_apns", payload);
        }

        if (pushType == PNPushType.GCM) {
            pushData.put("pn_gcm", payload);
        }

        if (pushType == PNPushType.MPNS) {
            pushData.put("pn_mpns", payload);
        }
    }

}
