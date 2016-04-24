package com.pubnub.api.callbacks;

import com.pubnub.api.core.PubNub;
import com.pubnub.api.core.models.consumer_facing.PNMessageResult;
import com.pubnub.api.core.models.consumer_facing.PNPresenceEventResult;
import com.pubnub.api.core.models.consumer.PNStatus;

public abstract class SubscribeCallback {
    public abstract void status(PubNub pubnub, PNStatus status);
    public abstract void message(PubNub pubnub, PNMessageResult message);
    public abstract void presence(PubNub pubnub, PNPresenceEventResult presence);
}
