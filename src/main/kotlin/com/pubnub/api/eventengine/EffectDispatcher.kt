package com.pubnub.api.eventengine

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

interface ManagedEffect {
    fun runEffect()
    fun cancel()
}

class EffectDispatcher<T : EffectInvocation>(
    private val managedEffectFactory: ManagedEffectFactory<T>,
    private val managedEffects: ConcurrentHashMap<String, ManagedEffect> = ConcurrentHashMap()
) {

    private val log = LoggerFactory.getLogger(EffectDispatcher::class.java)

    fun dispatch(effectInvocation: T) {
        log.trace("Dispatching effect: {}", effectInvocation)
        when (val type = effectInvocation.type) {
            is Cancel -> {
                managedEffects.remove(type.idToCancel)?.cancel()
            }

            is Managed -> {
                managedEffects.remove(effectInvocation.id)?.cancel()
                val managedEffect = managedEffectFactory.create(effectInvocation) ?: return
                managedEffects[effectInvocation.id] = managedEffect
                managedEffect.runEffect()
            }
            is NonManaged -> {
                managedEffectFactory.create(effectInvocation)?.runEffect()
            }
        }
    }
}
