package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.worker.EventConsumerWorker
import java.util.concurrent.LinkedBlockingQueue

class EventEngine {
    private val eventQueue: LinkedBlockingQueue<Event>
    private val effectQueue: LinkedBlockingQueue<EffectInvocation>
    private val eventConsumerThread: Thread
    private val flowQueue: LinkedBlockingQueue<Any>

    constructor(
        state: State,
        eventQueue: LinkedBlockingQueue<Event>,
        effectQueue: LinkedBlockingQueue<EffectInvocation>,
        flowQueue: LinkedBlockingQueue<Any>
    ) {
        this.eventQueue = eventQueue
        this.effectQueue = effectQueue
        this.flowQueue = flowQueue
        // parametr w kontruktrze, może mieć defaultowa wartość
        val eventEngineWorker: EventConsumerWorker =
            EventConsumerWorker(state, eventQueue, effectQueue, flowQueue, testMode = false)
        this.eventConsumerThread = Thread(eventEngineWorker)

        eventConsumerThread.name = "EventConsumerThread Consumer Thread"
        eventConsumerThread.isDaemon = true
        eventConsumerThread.start()
    }

    fun performTransitionEmitEffects(event: Event) {
        eventQueue.add(event)
    }
}
