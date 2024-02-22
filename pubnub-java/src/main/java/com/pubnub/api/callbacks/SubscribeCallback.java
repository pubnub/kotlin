package com.pubnub.api.callbacks;

import com.pubnub.api.v2.callbacks.EventListener;
import com.pubnub.api.v2.callbacks.StatusListener;

abstract public class SubscribeCallback implements StatusListener, EventListener { }
//
//    @Override
//    public final void objects(@NotNull BasePubNubImpl pubnub, @NotNull PNObjectEventResult objectEvent) {
//        PNObjectEventMessage message = objectEvent.getExtractedMessage();
//        if (message instanceof PNDeleteMembershipEventMessage) {
//            PNMembershipResult result = getDeleteMembershipResult(objectEvent, (PNDeleteMembershipEventMessage) message);
//            membership((PubNub) pubnub, result);
//        } else if (message instanceof PNSetMembershipEventMessage) {
//            PNMembershipResult result = getSetMembershipResult(objectEvent, (PNSetMembershipEventMessage) message);
//            membership((PubNub) pubnub, result);
//        } else if (message instanceof PNDeleteChannelMetadataEventMessage) {
//            PNChannelMetadataResult result = getDeleteChannelMetadataResult(objectEvent, (PNDeleteChannelMetadataEventMessage) message);
//            channel((PubNub) pubnub, result);
//        } else if (message instanceof PNSetChannelMetadataEventMessage) {
//            PNChannelMetadataResult result = getSetChannelMetadataResult(objectEvent, (PNSetChannelMetadataEventMessage) message);
//            channel((PubNub) pubnub, result);
//        } else if (message instanceof PNDeleteUUIDMetadataEventMessage) {
//            PNUUIDMetadataResult result = getDeleteUuidMetadataResult(objectEvent, (PNDeleteUUIDMetadataEventMessage) message);
//            uuid((PubNub) pubnub, result);
//        } else if (message instanceof PNSetUUIDMetadataEventMessage) {
//            PNUUIDMetadataResult result = getSetUuidMetadataResult(objectEvent, (PNSetUUIDMetadataEventMessage) message);
//            uuid((PubNub) pubnub, result);
//        }
//    }
//
//    @NotNull
//    private static PNUUIDMetadataResult getSetUuidMetadataResult(PNObjectEventResult objectEvent, PNSetUUIDMetadataEventMessage message) {
//        com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata data = message.getData();
//        PNUUIDMetadata newData = new PNUUIDMetadata(
//                data.getId(), data.getName()
//        );
//        newData.setType(data.getType());
//        newData.setStatus(data.getStatus());
//        newData.setCustom(data.getCustom());
//        newData.setUpdated(data.getUpdated());
//        newData.setETag(data.getETag());
//        newData.setEmail(data.getEmail());
//        newData.setExternalId(data.getExternalId());
//        newData.setProfileUrl(data.getProfileUrl());
//        return new PNUUIDMetadataResult(
//                message.getEvent(),
//                newData,
//                objectEvent.getChannel(),
//                objectEvent.getSubscription(),
//                objectEvent.getTimetoken(),
//                objectEvent.getUserMetadata(),
//                objectEvent.getPublisher()
//        );
//    }
//
//    @NotNull
//    private static PNUUIDMetadataResult getDeleteUuidMetadataResult(PNObjectEventResult objectEvent, PNDeleteUUIDMetadataEventMessage message) {
//        return new PNUUIDMetadataResult(
//                message.getEvent(),
//                new PNUUIDMetadata(
//                        message.getUuid(), null
//                ),
//                objectEvent.getChannel(),
//                objectEvent.getSubscription(),
//                objectEvent.getTimetoken(),
//                objectEvent.getUserMetadata(),
//                objectEvent.getPublisher()
//        );
//    }
//
//    @NotNull
//    private static PNChannelMetadataResult getSetChannelMetadataResult(PNObjectEventResult objectEvent, PNSetChannelMetadataEventMessage message) {
//        com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata data = message.getData();
//        PNChannelMetadata newData = new PNChannelMetadata(
//                data.getId(), data.getName(), data.getDescription()
//        );
//        newData.setType(data.getType());
//        newData.setStatus(data.getStatus());
//        newData.setCustom(data.getCustom());
//        newData.setUpdated(data.getUpdated());
//        newData.setETag(data.getETag());
//        return new PNChannelMetadataResult(
//                message.getEvent(),
//                objectEvent.getChannel(),
//                objectEvent.getSubscription(),
//                objectEvent.getTimetoken(),
//                objectEvent.getUserMetadata(),
//                objectEvent.getPublisher(),
//                newData
//        );
//    }
//
//    @NotNull
//    private static PNChannelMetadataResult getDeleteChannelMetadataResult(PNObjectEventResult objectEvent, PNDeleteChannelMetadataEventMessage message) {
//        return new PNChannelMetadataResult(
//                message.getEvent(),
//                objectEvent.getChannel(),
//                objectEvent.getSubscription(),
//                objectEvent.getTimetoken(),
//                objectEvent.getUserMetadata(),
//                objectEvent.getPublisher(),
//                new PNChannelMetadata(
//                        message.getChannel(), null
//                )
//        );
//    }
//
//    @NotNull
//    private static PNMembershipResult getDeleteMembershipResult(@NotNull PNObjectEventResult objectEvent, PNDeleteMembershipEventMessage message) {
//        PNMembership membership = new PNMembership(
//                new PNChannelMetadata(message.getData().getChannelId(), null)
//        );
//        membership.setUuid(message.getData().getUuid());
//        membership.setCustom(message);
//        return new PNMembershipResult(
//                message.getEvent(),
//                membership,
//                objectEvent.getChannel(),
//                objectEvent.getSubscription(),
//                objectEvent.getTimetoken(),
//                objectEvent.getUserMetadata(),
//                objectEvent.getPublisher()
//        );
//    }
//
//    @NotNull
//    private static PNMembershipResult getSetMembershipResult(@NotNull PNObjectEventResult objectEvent, PNSetMembershipEventMessage message) {
//        PNSetMembershipEvent data = message.getData();
//        PNMembership membership = new PNMembership(
//                new PNChannelMetadata(data.getChannel(), null)
//        );
//        membership.setUuid(data.getUuid());
//        membership.setCustom(data.getCustom());
//        membership.setStatus(data.getStatus());
//        membership.setUpdated(data.getUpdated());
//        membership.setETag(data.getETag());
//        return new PNMembershipResult(
//                message.getEvent(),
//                membership,
//                objectEvent.getChannel(),
//                objectEvent.getSubscription(),
//                objectEvent.getTimetoken(),
//                objectEvent.getUserMetadata(),
//                objectEvent.getPublisher()
//        );
//    }
//
//    public abstract void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus);
//
//    public abstract void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult);
//
//    public abstract void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult);
//
//    public abstract void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult);
//
//    public abstract void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult);
//
//    public abstract void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult);
//
//    public abstract void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult);
//
//    public abstract void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult);
//
//    public abstract void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult);
//
//    @Override
//    public void status(@NotNull BasePubNubImpl pubnub, @NotNull PNStatus status) {
//        status((PubNub) pubnub, status);
//    }
//
//    @Override
//    public void message(@NotNull BasePubNubImpl pubnub, @NotNull PNMessageResult message) {
//        message((PubNub) pubnub, message);
//    }
//
//    @Override
//    public void presence(@NotNull BasePubNubImpl pubnub, @NotNull PNPresenceEventResult presenceEvent) {
//        presence((PubNub) pubnub, presenceEvent);
//    }
//
//    @Override
//    public void signal(@NotNull BasePubNubImpl pubnub, @NotNull PNSignalResult signal) {
//        signal((PubNub) pubnub, signal);
//    }
//
//    @Override
//    public void messageAction(@NotNull BasePubNubImpl pubnub, @NotNull PNMessageActionResult messageAction) {
//        messageAction((PubNub) pubnub, messageAction);
//    }
//
//    @Override
//    public void file(@NotNull BasePubNubImpl pubnub, @NotNull PNFileEventResult fileEvent) {
//        file((PubNub) pubnub, fileEvent);
//    }
//
//    public static class BaseSubscribeCallback extends SubscribeCallback {
//
//        @Override
//        public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {
//
//        }
//
//        @Override
//        public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
//
//        }
//
//        @Override
//        public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult) {
//
//        }
//
//        @Override
//        public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult) {
//
//        }
//
//        @Override
//        public void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {
//
//        }
//
//        @Override
//        public void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult) {
//
//        }
//
//        @Override
//        public void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult) {
//
//        }
//
//        @Override
//        public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult) {
//
//        }
//
//        @Override
//        public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {
//
//        }
//    }
//}
