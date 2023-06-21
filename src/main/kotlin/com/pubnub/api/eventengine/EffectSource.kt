package com.pubnub.api.eventengine

interface EffectSource<T> {
    fun take(): T
}
