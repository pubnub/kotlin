package com.pubnub.api.eventengine

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class EffectDispatcher<T : EffectInvocation>(
    private val effectSource: EffectSource<T>,
    private val effectFactory: EffectFactory<T>,
    private val managedEffects: ConcurrentHashMap<String, ManagedEffect> = ConcurrentHashMap(),
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {
    private var task: Future<*>? = null
    fun start() {
        task = executorService.submit {
            while (true) {
                try {
                    val effect = effectSource.next()
                    dispatch(effect)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
    }

    private val log = LoggerFactory.getLogger(EffectDispatcher::class.java)

    fun dispatch(effectInvocation: T) {
        log.trace("Dispatching effect: {}", effectInvocation)
        when (val type = effectInvocation.type) {
            is Cancel -> {
                managedEffects.remove(type.idToCancel)?.cancel()
            }

            is Managed -> {
                managedEffects.remove(effectInvocation.id)?.cancel()
                val managedEffect = effectFactory.create(effectInvocation) as? ManagedEffect ?: return
                managedEffects[effectInvocation.id] = managedEffect
                managedEffect.runEffect()
            }

            is NonManaged -> {
                effectFactory.create(effectInvocation)?.runEffect()
            }
        }
    }
}
