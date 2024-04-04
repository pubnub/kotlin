package com.pubnub.api.v2.callbacks;


import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for
 * PubNub connection status changes.
 */
public interface StatusListener extends BaseStatusListener {
    void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus);
}