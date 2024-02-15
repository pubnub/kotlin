package com.pubnub.api.v2.callbacks;

import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.internal.BasePubNub;
import org.jetbrains.annotations.NotNull;

public abstract class StatusListener implements com.pubnub.internal.v2.callbacks.StatusListener {
    public abstract void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus);

    @Override
    public void status(@NotNull BasePubNub pubnub, @NotNull PNStatus status) {
        status((PubNub) pubnub, status);
    }
}
