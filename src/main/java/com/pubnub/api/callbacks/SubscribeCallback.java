package com.pubnub.api.callbacks;

import com.pubnub.api.core.models.PNSubscriberData;
import com.pubnub.api.core.models.PNPresenceEventResult;
import com.pubnub.api.core.models.PNStatus;

public abstract class SubscribeCallback {
    public abstract void status(PNStatus status);
    public abstract void message(PNSubscriberData message);
    public abstract void presence(PNPresenceEventResult presence);
}
