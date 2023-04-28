package com.pubnub.api.eventengine

interface ManagedEffectFactory<T : EffectInvocation> {
    fun create(effectInvocation: T): ManagedEffect?
}
