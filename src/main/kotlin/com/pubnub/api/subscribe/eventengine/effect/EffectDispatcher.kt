package com.pubnub.api.subscribe.eventengine.effect

import java.util.concurrent.ConcurrentHashMap

interface ManagedEffect {
    val id: String
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
                val managedEffect = effectHandlerFactory.create(effectInvocation)
                managedEffects.remove(managedEffect.id)?.cancel()
                managedEffects[managedEffect.id] = managedEffect
                managedEffect.run {
                    managedEffects.remove(managedEffect.id)
                }
            }

            else -> {
                effectHandlerFactory.create(effectInvocation).run()
            }
        }
    }
}
