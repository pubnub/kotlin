package com.pubnub.api.v2.callbacks

import com.pubnub.internal.v2.callbacks.Emitter

interface StatusEmitter : Emitter<StatusListener> {
    override fun addListener(listener: StatusListener)
    override fun removeListener(listener: StatusListener)
    override fun removeAllListeners()
}
