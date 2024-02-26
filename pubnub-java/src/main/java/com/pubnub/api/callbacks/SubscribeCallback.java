package com.pubnub.api.callbacks;

import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.v2.callbacks.EventListener;
import com.pubnub.api.v2.callbacks.StatusListener;
import org.jetbrains.annotations.NotNull;

public abstract class SubscribeCallback implements StatusListener, EventListener {
    public static class BaseSubscribeCallback extends SubscribeCallback {
        @Override
        public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {
        }
    }
}