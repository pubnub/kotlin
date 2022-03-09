package com.pubnub.api.state

import java.util.*

abstract class Effect(val id: String = UUID.randomUUID().toString()) {
    abstract val child: Effect?
}

interface EffectEngine<EF : Effect> {
    fun execute(effect: EF)

    fun cancel()
}

interface EffectExecutor<EF : Effect> {
    fun execute(effect: EF, longRunningEffectDone: (String) -> Unit = {}): CancelFn
}

typealias CancelFn = () -> Unit
