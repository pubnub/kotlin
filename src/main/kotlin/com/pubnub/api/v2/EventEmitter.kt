package com.pubnub.api.v2

import com.pubnub.api.v2.callbacks.EventListener

interface EventEmitter {
    fun addListener(listener: EventListener)
    fun removeListener(listener: EventListener)
    fun removeAllListeners()
}
