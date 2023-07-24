package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult;
import com.pubnub.api.subscribe.eventengine.effect.MessagesConsumer;
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager implements StatusConsumer<PNStatus>, MessagesConsumer {

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

    public void announce(final PNUUIDMetadataResult uuidMetadataResult) {
        for (final SubscribeCallback subscribeCallback: getListeners()) {
            subscribeCallback.uuid(this.pubnub, uuidMetadataResult);
        }
    }

    public void announce(final PNChannelMetadataResult channelMetadataResult) {
        for (final SubscribeCallback subscribeCallback: getListeners()) {
            subscribeCallback.channel(this.pubnub, channelMetadataResult);
        }
    }

    public void announce(final PNMembershipResult membershipResult) {
        for (final SubscribeCallback subscribeCallback: getListeners()) {
            subscribeCallback.membership(this.pubnub, membershipResult);
        }
    }

    public void announce(PNMessageActionResult messageAction) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.messageAction(this.pubnub, messageAction);
        }
    }

    public void announce(PNFileEventResult fileEventResult) {
        for (SubscribeCallback subscribeCallback : getListeners()) {
            subscribeCallback.file(this.pubnub, fileEventResult);
        }
    }

    @Override
    public void announce(@NotNull PNObjectEventResult pnObjectEventResult) {
        throw new UnsupportedOperationException(); // TODO: implement
    }
}
