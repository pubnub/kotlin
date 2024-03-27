package com.pubnub.internal.eventengine

import org.slf4j.LoggerFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class EventEngine<Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>>(
    private val effectSink: Sink<Ei>,
    private val eventSource: Source<Ev>,
    private var currentState: S,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor(),
) {
    private val log = LoggerFactory.getLogger(EventEngine::class.java)

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
            "Current state is: ${currentState::class.simpleName} ; ${
                event::class.java.name.substringAfterLast('.').substringBefore('$')
            } to be handled is: $event ",
        )
        val (newState, invocations) = transition(currentState, event)
        currentState = newState
        invocations.forEach { invocation -> effectSink.add(invocation) }
    }
}
