package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNMembershipResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNSpaceResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNUserResult;

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

    public void announce(PNMessageResult message) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.message(this.pubnub, message);
        }
    }

    public void announce(PNPresenceEventResult presence) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.presence(this.pubnub, presence);
        }
    }

    public void announce(PNSignalResult signal) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.signal(this.pubnub, signal);
        }
    }

    public void announce(PNUserResult user) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.user(this.pubnub, user);
        }
    }

    public void announce(PNSpaceResult space) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.space(this.pubnub, space);
        }
    }

    public void announce(PNMembershipResult membership) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.membership(this.pubnub, membership);
        }
    }

    public void announce(PNMessageActionResult messageAction) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.messageAction(this.pubnub, messageAction);
        }
    }

}
