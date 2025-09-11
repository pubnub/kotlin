package com.pubnub.internal.eventengine

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.logging.LoggerManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class EventEngine<Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>>(
    private val effectSink: Sink<Ei>,
    private val eventSource: Source<Ev>,
    private var currentState: S,
    private val logConfig: LogConfig,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor(),
) {
    private val log = LoggerManager.instance.getLogger(logConfig, this::class.java)

    fun start() {
        executorService.submit {
            try {
                while (true) {
                    val event = eventSource.take()
                    performTransitionAndEmitEffects(event)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    fun stop() {
        executorService.shutdownNow()
    }

    internal fun performTransitionAndEmitEffects(event: Ev) {
        log.trace(
            LogMessage(
                message = LogMessageContent.Text(
                    "Current state is: ${currentState::class.simpleName} ; ${
                        event::class.java.name.substringAfterLast('.').substringBefore('$')
                    } to be handled is: $event ",
                ),
            )
        )
        val (newState, invocations) = transition(currentState, event)
        log.trace(
            LogMessage(
                message = LogMessageContent.Text(
                    "New state is: ${newState::class.simpleName} ; Emitting fallowing effects: ${
                        invocations.joinToString { it::class.java.name.substringAfterLast('.').substringAfter('$') }
                    }",
                ),
            )
        )
        currentState = newState
        invocations.forEach { invocation -> effectSink.add(invocation) }
    }
}
