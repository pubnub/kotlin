package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.eventengine.transition
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SubscribeEventEngine(
    val effectSink: Sink<SubscribeEffectInvocation>,
    private val eventSource: Source<SubscribeEvent>,
    private var currenState: SubscribeState = SubscribeState.Unsubscribed,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor(),
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

    internal fun performTransitionAndEmitEffects(subscribeEvent: SubscribeEvent) { // todo add unit tests
        val (newState, invocations) = transition(currenState, subscribeEvent)
        currenState = newState
        invocations.forEach { invocation -> effectSink.add(invocation) }
    }
}
