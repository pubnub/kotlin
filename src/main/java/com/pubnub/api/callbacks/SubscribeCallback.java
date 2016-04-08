package com.pubnub.api.callbacks;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.models.PNMessageResult;
import com.pubnub.api.core.models.PNPresenceEventResult;
import com.pubnub.api.core.models.PNStatus;

public abstract class SubscribeCallback {
    public abstract void status(Pubnub pubnub, PNStatus status);
    public abstract void message(Pubnub pubnub, PNMessageResult message);
    public abstract void presence(Pubnub pubnub, PNPresenceEventResult presence);
}
