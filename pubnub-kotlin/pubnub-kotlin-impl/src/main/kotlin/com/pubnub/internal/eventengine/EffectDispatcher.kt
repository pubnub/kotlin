package com.pubnub.internal.eventengine

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.internal.logging.LoggerManager
import org.slf4j.event.Level
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class EffectDispatcher<T : EffectInvocation>(
    private val effectFactory: EffectFactory<T>,
    private val effectSource: Source<T>,
    private val logConfig: LogConfig,
    private val managedEffects: ConcurrentHashMap<String, ManagedEffect> = ConcurrentHashMap(),
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    fun start() {
        executorService.submit {
            try {
                while (true) {
                    val invocation = effectSource.take()
                    dispatch(invocation)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    fun stop() {
        executorService.shutdownNow()
    }

    internal fun dispatch(effectInvocation: T) {
        log.trace(
            LogMessage(
                pubNubId = logConfig.pnInstanceId,
                logLevel = Level.TRACE,
                location = this::class.java.simpleName,
                type = LogMessageType.TEXT,
                message = LogMessageContent.Text("Dispatching effect: $effectInvocation"),
            )
        )

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
