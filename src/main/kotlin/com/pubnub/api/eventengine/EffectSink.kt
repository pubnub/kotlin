package com.pubnub.api.eventengine

interface EffectSink<T : EffectInvocation> {
    fun add(invocation: T)
}
