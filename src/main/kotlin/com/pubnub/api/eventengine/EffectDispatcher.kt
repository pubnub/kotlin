package com.pubnub.api.eventengine

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

interface ManagedEffect {
    fun run(completionBlock: () -> Unit = {})
    fun cancel()
}

class EffectDispatcher<T : EffectInvocation>(
    private val effectHandlerFactory: ManagedEffectFactory<T>,
    private val managedEffects: ConcurrentHashMap<String, ManagedEffect> = ConcurrentHashMap()
) {

    private val log = LoggerFactory.getLogger(EffectDispatcher::class.java)

    fun dispatch(effectInvocation: T) {
        log.trace("Dispatching effect: {}", effectInvocation)
        when (effectInvocation) {
            is CancelEffectInvocation -> {
                managedEffects.remove(effectInvocation.idToCancel)?.cancel()
            }

            is ManagedEffectInvocation -> {
                managedEffects.remove(effectInvocation.id)?.cancel()
                val managedEffect = effectHandlerFactory.create(effectInvocation) ?: return
                managedEffects[effectInvocation.id] = managedEffect
                managedEffect.run {
                    managedEffects.remove(effectInvocation.id)
                }
            }

            else -> {
                effectHandlerFactory.create(effectInvocation)?.run()
            }
        }
    }
}
