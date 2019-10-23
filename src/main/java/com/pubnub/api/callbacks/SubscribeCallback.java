package com.pubnub.api.callbacks;

import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNMembershipResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNSpaceResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNUserResult;

import org.jetbrains.annotations.NotNull;

public abstract class SubscribeCallback {
    public abstract void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus);

    public abstract void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult);

    public abstract void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult);

    public abstract void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult);

    public abstract void user(@NotNull PubNub pubnub, @NotNull PNUserResult pnUserResult);

    public abstract void space(@NotNull PubNub pubnub, @NotNull PNSpaceResult pnSpaceResult);

    public abstract void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult);

    public abstract void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult);
}
