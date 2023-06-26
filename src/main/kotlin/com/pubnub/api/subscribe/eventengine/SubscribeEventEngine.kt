package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SubscribeEventEngine(
    val effectSink: Sink<SubscribeEffectInvocation>,
    private val eventSource: Source<Event>,
    private var currenState: SubscribeState = SubscribeState.Unsubscribed,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor(),
) {

    fun start() {
        executorService.submit {
            while (true) { // todo może zmien na while (!Thread.interrupted()) ?
                try {
                    val event = eventSource.take()
                    performTransitionAndEmitEffects(event)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
    }

    fun stop() {
        executorService.shutdownNow()
    }

    internal fun performTransitionAndEmitEffects(event: Event) { // todo add unit tests
        val (newState, invocations) = transition(currenState, event)
        currenState = newState
        invocations.forEach { invocation -> effectSink.add(invocation) }
    }
}
