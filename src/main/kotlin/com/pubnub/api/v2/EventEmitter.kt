package com.pubnub.api.v2

import com.pubnub.api.callbacks.SubscribeCallback

interface EventEmitter {
    fun addListener(listener: SubscribeCallback)
    fun removeListener(listener: SubscribeCallback)
    fun removeAllListeners()
}
