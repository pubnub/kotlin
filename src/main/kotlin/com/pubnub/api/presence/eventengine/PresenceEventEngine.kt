package com.pubnub.api.presence.eventengine

import com.pubnub.api.eventengine.EventEngine
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.eventengine.transition
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PresenceEventEngine(
    private val effectSink: Sink<PresenceEffectInvocation>,
    private val eventSource: Source<PresenceEvent>,
    private var currentState: PresenceState = PresenceState.HearbeatInactive,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) : EventEngine {

    override fun start() {
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

    override fun stop() {
        executorService.shutdownNow()
    }

    internal fun performTransitionAndEmitEffects(presenceEvent: PresenceEvent) { // todo add unit tests
        val (newState, invocations) = transition(currentState, presenceEvent)
        currentState = newState
        invocations.forEach { invocation -> effectSink.add(invocation) }
    }
}
