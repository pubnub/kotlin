package com.pubnub.api.eventengine

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EventEngine<Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>>(
    private val effectSink: Sink<Ei>,
    private val eventSource: Source<Ev>,
    private var currentState: S,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {

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
        val (newState, invocations) = transition(currentState, event)
        currentState = newState
        invocations.forEach { invocation -> effectSink.add(invocation) }
    }
}
