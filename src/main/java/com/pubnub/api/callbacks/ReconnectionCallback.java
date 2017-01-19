package com.pubnub.api.callbacks;


public abstract class ReconnectionCallback {

    public abstract void onReconnection();

    public abstract void onMaxReconnectionExhaustion();

}
