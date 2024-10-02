package com.pubnub.internal.java.v2.callbacks;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataConverter;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataConverter;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage;
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage;
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage;
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventMessage;
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage;
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent;
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage;
import com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage;
import org.jetbrains.annotations.NotNull;

class Converters {
    private Converters() {
    }

    static ObjectResult<?> objects(@NotNull PNObjectEventResult objectEvent) {
        PNObjectEventMessage message = objectEvent.getExtractedMessage();
        if (message instanceof PNDeleteMembershipEventMessage) {
            return getDeleteMembershipResult(objectEvent, (PNDeleteMembershipEventMessage) message);
        } else if (message instanceof PNSetMembershipEventMessage) {
            return getSetMembershipResult(objectEvent, (PNSetMembershipEventMessage) message);
        } else if (message instanceof PNDeleteChannelMetadataEventMessage) {
            return getDeleteChannelMetadataResult(objectEvent, (PNDeleteChannelMetadataEventMessage) message);
        } else if (message instanceof PNSetChannelMetadataEventMessage) {
            return getSetChannelMetadataResult(objectEvent, (PNSetChannelMetadataEventMessage) message);
        } else if (message instanceof PNDeleteUUIDMetadataEventMessage) {
            return getDeleteUuidMetadataResult(objectEvent, (PNDeleteUUIDMetadataEventMessage) message);
        } else if (message instanceof PNSetUUIDMetadataEventMessage) {
            return getSetUuidMetadataResult(objectEvent, (PNSetUUIDMetadataEventMessage) message);
        }
        return null;
    }

    static void objects(@NotNull PNObjectEventResult objectEvent, @NotNull EventListener emitter, @NotNull PubNub pubnub) {
        PNObjectEventMessage message = objectEvent.getExtractedMessage();
        if (message instanceof PNDeleteMembershipEventMessage) {
            PNMembershipResult result = getDeleteMembershipResult(objectEvent, (PNDeleteMembershipEventMessage) message);
            emitter.membership(pubnub, result);
        } else if (message instanceof PNSetMembershipEventMessage) {
            PNMembershipResult result = getSetMembershipResult(objectEvent, (PNSetMembershipEventMessage) message);
            emitter.membership(pubnub, result);
        } else if (message instanceof PNDeleteChannelMetadataEventMessage) {
            PNChannelMetadataResult result = getDeleteChannelMetadataResult(objectEvent, (PNDeleteChannelMetadataEventMessage) message);
            emitter.channel(pubnub, result);
        } else if (message instanceof PNSetChannelMetadataEventMessage) {
            PNChannelMetadataResult result = getSetChannelMetadataResult(objectEvent, (PNSetChannelMetadataEventMessage) message);
            emitter.channel(pubnub, result);
        } else if (message instanceof PNDeleteUUIDMetadataEventMessage) {
            PNUUIDMetadataResult result = getDeleteUuidMetadataResult(objectEvent, (PNDeleteUUIDMetadataEventMessage) message);
            emitter.uuid(pubnub, result);
        } else if (message instanceof PNSetUUIDMetadataEventMessage) {
            PNUUIDMetadataResult result = getSetUuidMetadataResult(objectEvent, (PNSetUUIDMetadataEventMessage) message);
            emitter.uuid(pubnub, result);
        }
    }

    @NotNull
    static PNUUIDMetadataResult getSetUuidMetadataResult(PNObjectEventResult objectEvent, PNSetUUIDMetadataEventMessage message) {
        com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata data = message.getData();
        PNUUIDMetadata newData = PNUUIDMetadataConverter.from(data);

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
        com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata data = message.getData();
        PNChannelMetadata newData = PNChannelMetadataConverter.from(data);
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
}
