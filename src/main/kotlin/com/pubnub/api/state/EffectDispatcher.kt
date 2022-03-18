package com.pubnub.api.state

interface EffectDispatcher<EF> {
    fun dispatch(effect: EF)
}
