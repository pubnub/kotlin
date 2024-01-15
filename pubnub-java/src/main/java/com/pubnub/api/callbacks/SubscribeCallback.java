package com.pubnub.api.callbacks;

import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import org.jetbrains.annotations.NotNull;

public abstract class SubscribeCallback {
    public abstract void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus);

    public abstract void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult);

    public abstract void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult);

    public abstract void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult);

    public abstract void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult);

    public abstract void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult);

    public abstract void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult);

    public abstract void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult);

    public abstract void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult);

    public static class BaseSubscribeCallback extends SubscribeCallback {

        @Override
        public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {

        }

        @Override
        public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {

        }

        @Override
        public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult) {

        }

        @Override
        public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult) {

        }

        @Override
        public void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {

        }

        @Override
        public void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult) {

        }

        @Override
        public void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult) {

        }

        @Override
        public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult) {

        }

        @Override
        public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

        }
    }
}
