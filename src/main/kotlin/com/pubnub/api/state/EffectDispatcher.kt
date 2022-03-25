package com.pubnub.api.state

interface EffectDispatcher<EF : EffectInvocation> {
    fun dispatch(effect: EF)
}

