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

public abstract class SubscribeCallback {
    public abstract void status(PubNub pubnub, PNStatus pnStatus);

    public abstract void message(PubNub pubnub, PNMessageResult pnMessageResult);

    public abstract void presence(PubNub pubnub, PNPresenceEventResult pnPresenceEventResult);

    public abstract void signal(PubNub pubnub, PNSignalResult pnSignalResult);

    public abstract void user(PubNub pubnub, PNUserResult pnUserResult);

    public abstract void space(PubNub pubnub, PNSpaceResult pnSpaceResult);

    public abstract void membership(PubNub pubnub, PNMembershipResult pnMembershipResult);

    public abstract void messageAction(PubNub pubnub, PNMessageActionResult pnMessageActionResult);
}
