package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import java.util.concurrent.LinkedBlockingQueue

class EventConsumerWorker(
    private var currentState: State,
    private val eventQueue: LinkedBlockingQueue<Event>,
    private val effectQueue: LinkedBlockingQueue<EffectInvocation>,
) : Runnable {

    override fun run() {
        while (!Thread.interrupted()) {
            try {
                // what if eventQueue is empty?
                println("---EventEngineWorker--- queue size: " + eventQueue.size)
                val event: Event = eventQueue.take()
                performTransitionAndEmitEffects(event)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun performTransitionAndEmitEffects(event: Event) {
        println("---EventEngineWorker performs transition as reaction for event: $event")
        val (stateAfterTransition, transitionEffects) = transition(currentState, event)

        currentState = stateAfterTransition

        // add all transitionEffects into queue
        transitionEffects.forEach { effectQueue.add(it) }
    }
}
