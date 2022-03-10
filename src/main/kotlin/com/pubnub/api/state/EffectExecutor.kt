package com.pubnub.api.state

import java.util.*
import java.util.concurrent.CompletableFuture

abstract class Effect(val id: String = UUID.randomUUID().toString()) {
    abstract val child: Effect?
}

interface EffectDispatcher<EF : Effect> {
    fun dispatch(effect: EF)

    fun cancel()
}


//Would be nice if the declaration could tell the whole story (specific return type is missing)
//maybe instead of `CancelFn` something like `CompletableFuture<SpecificEvent>` would be better?
interface EffectExecutor<EF : Effect> {
    fun execute(effect: EF, longRunningEffectDone: (String) -> Unit = {}): CancelFn
}

typealias CancelFn = () -> Unit
