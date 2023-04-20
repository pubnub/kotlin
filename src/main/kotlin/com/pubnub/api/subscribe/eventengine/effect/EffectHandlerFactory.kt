package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.EffectInvocation
import com.pubnub.api.eventengine.ManagedEffect

interface EffectHandlerFactory<T : EffectInvocation> {
    fun create(effectInvocation: T): ManagedEffect
}
