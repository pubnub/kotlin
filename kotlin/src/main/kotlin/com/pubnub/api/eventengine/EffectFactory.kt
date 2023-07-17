package com.pubnub.api.eventengine

interface EffectFactory<T : EffectInvocation> {
    fun create(effectInvocation: T): Effect?
}
