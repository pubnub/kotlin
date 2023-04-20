package com.pubnub.api.eventengine

import java.util.concurrent.ConcurrentHashMap

interface ManagedEffect {
    fun run(completionBlock: () -> Unit = {})
    fun cancel()
}

class EffectDispatcher<T : EffectInvocation>(
    private val effectHandlerFactory: EffectHandlerFactory<T>,
    private val managedEffects: ConcurrentHashMap<String, ManagedEffect> = ConcurrentHashMap()
) {

    fun dispatch(effectInvocation: T) {
        when (effectInvocation) {
            is CancelEffectInvocation -> {
                managedEffects.remove(effectInvocation.idToCancel)?.cancel()
            }

            is ManagedEffectInvocation -> {
                managedEffects.remove(effectInvocation.id)?.cancel()
                val managedEffect = effectHandlerFactory.create(effectInvocation)
                managedEffects[effectInvocation.id] = managedEffect
                managedEffect.run {
                    managedEffects.remove(effectInvocation.id)
                }
            }

            else -> {
                effectHandlerFactory.create(effectInvocation).run()
            }
        }
    }
}
