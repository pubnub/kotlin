package com.pubnub.internal.v2.callbacks;

import com.pubnub.api.BasePubNub;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.objects.channel.PartialPNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.ConvertersKt;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.v2.callbacks.EventListener;
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage;
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage;
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage;
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventMessage;
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult;
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage;
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetMembershipEvent;
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetMembershipEventMessage;
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DelegatingEventListener implements EventListenerCore {
    private final EventListener listener;

    public DelegatingEventListener(EventListener listener) {
        this.listener = listener;
    }

    public EventListener getListener() {
        return listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DelegatingEventListener)) {
            return false;
        }
        DelegatingEventListener that = (DelegatingEventListener) o;
        return Objects.equals(listener, that.listener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listener);
    }

    @Override
    public final void objects(@NotNull BasePubNub<?, ?, ?, ?, ?, ?, ?, ?> pubnub, @NotNull PNObjectEventResult objectEvent) {
        PNObjectEventMessage message = objectEvent.getExtractedMessage();
        if (message instanceof PNDeleteMembershipEventMessage) {
            PNMembershipResult result = getDeleteMembershipResult(objectEvent, (PNDeleteMembershipEventMessage) message);
            listener.membership((PubNub) pubnub, result);
        } else if (message instanceof PNSetMembershipEventMessage) {
            PNMembershipResult result = getSetMembershipResult(objectEvent, (PNSetMembershipEventMessage) message);
            listener.membership((PubNub) pubnub, result);
        } else if (message instanceof PNDeleteChannelMetadataEventMessage) {
            PNChannelMetadataResult result = getDeleteChannelMetadataResult(objectEvent, (PNDeleteChannelMetadataEventMessage) message);
            listener.channel((PubNub) pubnub, result);
        } else if (message instanceof PNSetChannelMetadataEventMessage) {
            PNChannelMetadataResult result = getSetChannelMetadataResult(objectEvent, (PNSetChannelMetadataEventMessage) message);
            listener.channel((PubNub) pubnub, result);
        } else if (message instanceof PNDeleteUUIDMetadataEventMessage) {
            PNUUIDMetadataResult result = getDeleteUuidMetadataResult(objectEvent, (PNDeleteUUIDMetadataEventMessage) message);
            listener.uuid((PubNub) pubnub, result);
        } else if (message instanceof PNSetUUIDMetadataEventMessage) {
            PNUUIDMetadataResult result = getSetUuidMetadataResult(objectEvent, (PNSetUUIDMetadataEventMessage) message);
            listener.uuid((PubNub) pubnub, result);
        }
    }

    @NotNull
    static PNUUIDMetadataResult getSetUuidMetadataResult(PNObjectEventResult objectEvent, PNSetUUIDMetadataEventMessage message) {
        com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata data = message.getData();
        PNUUIDMetadata newData = PNUUIDMetadata.from(data);

        return new PNUUIDMetadataResult(
                message.getEvent(),
                newData,
                objectEvent.getChannel(),
                objectEvent.getSubscription(),
                objectEvent.getTimetoken(),
                objectEvent.getUserMetadata(),
                objectEvent.getPublisher()
        );
    }

    @NotNull
    static PNUUIDMetadataResult getDeleteUuidMetadataResult(PNObjectEventResult objectEvent, PNDeleteUUIDMetadataEventMessage message) {
        return new PNUUIDMetadataResult(
                message.getEvent(),
                new PNUUIDMetadata(
                        message.getUuid(), null
                ),
                objectEvent.getChannel(),
                objectEvent.getSubscription(),
                objectEvent.getTimetoken(),
                objectEvent.getUserMetadata(),
                objectEvent.getPublisher()
        );
    }

    @NotNull
    static PNChannelMetadataResult getSetChannelMetadataResult(PNObjectEventResult objectEvent, PNSetChannelMetadataEventMessage message) {
        PartialPNChannelMetadata data = message.getData();
        PNChannelMetadata newData = ConvertersKt.from(data);
        return new PNChannelMetadataResult(
                message.getEvent(),
                objectEvent.getChannel(),
                objectEvent.getSubscription(),
                objectEvent.getTimetoken(),
                objectEvent.getUserMetadata(),
                objectEvent.getPublisher(),
                newData
        );
    }

    @NotNull
    static PNChannelMetadataResult getDeleteChannelMetadataResult(PNObjectEventResult objectEvent, PNDeleteChannelMetadataEventMessage message) {
        return new PNChannelMetadataResult(
                message.getEvent(),
                objectEvent.getChannel(),
                objectEvent.getSubscription(),
                objectEvent.getTimetoken(),
                objectEvent.getUserMetadata(),
                objectEvent.getPublisher(),
                new PNChannelMetadata(
                        message.getChannel(), null
                )
        );
    }

    @NotNull
    static PNMembershipResult getDeleteMembershipResult(@NotNull PNObjectEventResult objectEvent, PNDeleteMembershipEventMessage message) {
        PNMembership membership = new PNMembership(
                new PNChannelMetadata(message.getData().getChannelId(), null)
        );
        membership.setUuid(message.getData().getUuid());
        return new PNMembershipResult(
                message.getEvent(),
                membership,
                objectEvent.getChannel(),
                objectEvent.getSubscription(),
                objectEvent.getTimetoken(),
                objectEvent.getUserMetadata(),
                objectEvent.getPublisher()
        );
    }

    @NotNull
    static PNMembershipResult getSetMembershipResult(@NotNull PNObjectEventResult objectEvent, PNSetMembershipEventMessage message) {
        PNSetMembershipEvent data = message.getData();
        PNMembership membership = new PNMembership(
                new PNChannelMetadata(data.getChannel(), null)
        );
        membership.setUuid(data.getUuid());
        membership.setCustom(data.getCustom());
        membership.setStatus(data.getStatus());
        membership.setUpdated(data.getUpdated());
        membership.setETag(data.getETag());
        return new PNMembershipResult(
                message.getEvent(),
                membership,
                objectEvent.getChannel(),
                objectEvent.getSubscription(),
                objectEvent.getTimetoken(),
                objectEvent.getUserMetadata(),
                objectEvent.getPublisher()
        );
    }


    @Override
    public void message(@NotNull BasePubNub<?, ?, ?, ?, ?, ?, ?, ?> pubnub, @NotNull PNMessageResult message) {
        listener.message((PubNub) pubnub, message);
    }

    @Override
    public void presence(@NotNull BasePubNub<?, ?, ?, ?, ?, ?, ?, ?> pubnub, @NotNull PNPresenceEventResult presenceEvent) {
        listener.presence((PubNub) pubnub, presenceEvent);
    }

    @Override
    public void signal(@NotNull BasePubNub<?, ?, ?, ?, ?, ?, ?, ?> pubnub, @NotNull PNSignalResult signal) {
        listener.signal((PubNub) pubnub, signal);
    }

    @Override
    public void messageAction(@NotNull BasePubNub<?, ?, ?, ?, ?, ?, ?, ?> pubnub, @NotNull PNMessageActionResult messageAction) {
        listener.messageAction((PubNub) pubnub, messageAction);
    }

    @Override
    public void file(@NotNull BasePubNub<?, ?, ?, ?, ?, ?, ?, ?> pubnub, @NotNull PNFileEventResult fileEvent) {
        listener.file((PubNub) pubnub, fileEvent);
    }
}
