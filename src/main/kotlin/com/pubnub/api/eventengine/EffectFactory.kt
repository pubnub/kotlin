package com.pubnub.api.eventengine

internal interface EffectFactory<T : EffectInvocation> {
    fun create(effectInvocation: T): Effect?
}
