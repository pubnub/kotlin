package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private List<SubscribeCallback> listeners;
    private PubNub pubnub;

    public ListenerManager(PubNub pubnubInstance) {
        this.listeners = new ArrayList<>();
        this.pubnub = pubnubInstance;
    }

    public void addListener(SubscribeCallback listener) {
        listeners.add(listener);
    }

    public void removeListener(SubscribeCallback listener) {
        listeners.remove(listener);
    }

    /**
     * announce a PNStatus to listeners.
     *
     * @param status PNStatus which will be broadcast to listeners.
     */
    public void announce(PNStatus status) {
        for (SubscribeCallback subscribeCallback : listeners) {
            subscribeCallback.status(this.pubnub, status);
        }
    }

    public void announce(PNMessageResult message) {
        for (SubscribeCallback subscribeCallback : listeners) {
            subscribeCallback.message(this.pubnub, message);
        }
    }

    public void announce(PNPresenceEventResult presence) {
        for (SubscribeCallback subscribeCallback : listeners) {
            subscribeCallback.presence(this.pubnub, presence);
        }
    }

}
