package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.eventengine.EffectSink
import com.pubnub.api.eventengine.EventSource
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SubscribeEventEngine(
    val effectSink: EffectSink<SubscribeEffectInvocation>,
    private val eventSource: EventSource,
    private var currenState: SubscribeState = SubscribeState.Unsubscribed,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor(),
) {
    internal var isStarted = false

    fun start() {
        isStarted = true
        executorService.submit {
            while (true) {
                try {
                    val event = eventSource.take()
                    performTransitionAndEmitEffects(event)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    isStarted = false
                }
            }
        }
    }

    private fun performTransitionAndEmitEffects(event: Event) {
        val (newState, invocations) = transition(currenState, event)
        currenState = newState
        invocations.forEach { invocation -> effectSink.add(invocation) }
    }
}
