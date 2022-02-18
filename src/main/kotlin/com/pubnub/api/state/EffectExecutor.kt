package com.pubnub.api.state

import java.util.*

abstract class Effect(val id: String = UUID.randomUUID().toString()) {
    abstract val child: Effect?
}

interface EffectExecutor<EF : Effect> {
    fun execute(effect: EF, longRunningEffectDone: (String) -> Unit = {}): CancelFn
}

typealias CancelFn = () -> Unit
