package com.pubnub.api.callbacks

internal abstract class ReconnectionCallback {
    abstract fun onReconnection()
    abstract fun onMaxReconnectionExhaustion()
}
