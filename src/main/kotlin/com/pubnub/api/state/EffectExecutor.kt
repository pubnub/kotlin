package com.pubnub.api.state

interface EffectExecutor<EF : Effect> {
    fun execute(effect: EF, longRunningEffectDone: (String) -> Unit = {}): CancelFn
}

typealias CancelFn = () -> Unit
