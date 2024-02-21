package com.pubnub.internal.callbacks

internal abstract class ReconnectionCallback {
    abstract fun onReconnection()
    abstract fun onMaxReconnectionExhaustion()
}
