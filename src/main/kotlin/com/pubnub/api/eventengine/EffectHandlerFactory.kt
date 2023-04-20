package com.pubnub.api.eventengine

interface EffectHandlerFactory<T : EffectInvocation> {
    fun create(effectInvocation: T): ManagedEffect
}
