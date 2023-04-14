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
    private val flowQueue: LinkedBlockingQueue<Any>, // queue that is used for testing purpose to track changes in EventEngine maybe also in EffectDispatcher ?
    private val testMode: Boolean
) : Runnable {

    override fun run() {
        while (!Thread.interrupted()) {
            try {
                // what if eventQueue is empty?
                println("---EventEngineWorker--- queue size: " + eventQueue.size)
                val event: Event = eventQueue.take()
                addToFLowQueue(currentState)
                addToFLowQueue(event)
                performTransitionAndEmitEffects(event)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun addToFLowQueue(eventOrStateOrTransition: Any) {
        if (testMode) {
            flowQueue.add(eventOrStateOrTransition)
        }
    }

    private fun performTransitionAndEmitEffects(event: Event) {
        println("---EventEngineWorker performs transition as reaction for event: $event")
        val (stateAfterTransition, transitionEffects) = transition(currentState, event)

        currentState = stateAfterTransition
        addToFLowQueue(currentState)

        // add all transitionEffects into queue
        transitionEffects.forEach { effectQueue.add(it) }
    }
}
