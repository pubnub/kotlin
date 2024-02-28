package com.pubnub.internal.eventengine

internal interface EffectFactory<T : EffectInvocation> {
    fun create(effectInvocation: T): Effect?
}
