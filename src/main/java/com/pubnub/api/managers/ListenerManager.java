package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {

    private final List<SubscribeCallback> listeners;
    private final PubNub pubnub;

    public ListenerManager(PubNub pubnubInstance) {
        this.listeners = new ArrayList<>();
        this.pubnub = pubnubInstance;
    }

    public void addListener(SubscribeCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeListener(SubscribeCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private List<SubscribeCallback> getListeners() {
        List<SubscribeCallback> tempCallbackList = new ArrayList<>();
        synchronized (listeners) {
            tempCallbackList.addAll(listeners);
        }
        return tempCallbackList;
    }

    /**
     * announce a PNStatus to listeners.
     *
     * @param status PNStatus which will be broadcast to listeners.
     */
    public void announce(PNStatus status) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.status(this.pubnub, status);
        }
    }

    public void announce(PNMessageResult message, Integer type) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            if (type == null) {
                subscribeCallback.message(this.pubnub, message);
            } else if (type == 1) {
                subscribeCallback.signal(this.pubnub, message);
            }
        }
    }

    public void announce(PNPresenceEventResult presence) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.presence(this.pubnub, presence);
        }
    }

}
