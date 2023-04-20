package com.pubnub.api.subscribe.eventengine.effect

interface EffectHandlerFactory<T : EffectInvocation> {
    fun create(effectInvocation: T): ManagedEffect
}
