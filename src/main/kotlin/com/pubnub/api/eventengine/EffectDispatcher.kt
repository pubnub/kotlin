package com.pubnub.api.eventengine

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EffectDispatcher<T : EffectInvocation>(
    private val effectFactory: EffectFactory<T>,
    private val managedEffects: ConcurrentHashMap<String, ManagedEffect> = ConcurrentHashMap(),
    private val effectSource: EffectSource<T>,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

) {
    private val log = LoggerFactory.getLogger(EffectDispatcher::class.java)
    internal var isStarted = false

    fun start() {
        isStarted = true
        executorService.submit {
            try {
                while (true) {
                    val invocation = effectSource.take() // todo added missing unit tests
                    dispatch(invocation)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                isStarted = false
            }
        }
    }

    fun dispatch(effectInvocation: T) {
        log.trace("Dispatching effect: $effectInvocation thread: ${Thread.currentThread().id}")
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
