package com.pubnub.api.v2.callbacks;

import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import org.jetbrains.annotations.NotNull;

public interface EventListener extends BaseEventListener {

    default void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {}

    default void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult) {}

    default void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult) {}

    default void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {}

    default void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult) {}

    default void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult) {}

    default void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult) {}

    default void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {}

}

