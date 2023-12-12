package com.pubnub.api.v2.callbacks

import com.pubnub.internal.v2.callbacks.Emitter

interface EventEmitter : Emitter<EventListener> {
    override fun addListener(listener: EventListener)
    override fun removeListener(listener: EventListener)
    override fun removeAllListeners()
}
