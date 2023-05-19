package com.pubnub.api.eventengine

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class EffectDispatcher<T : EffectInvocation>(
    private val effectFactory: EffectFactory<T>,
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
                val managedEffect = effectFactory.create(effectInvocation) ?: return
                managedEffects[effectInvocation.id] = managedEffect as ManagedEffect
                managedEffect.runEffect()
            }
            is NonManaged -> {
                effectFactory.create(effectInvocation)?.runEffect()
            }
        }
    }
}
