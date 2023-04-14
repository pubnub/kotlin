package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.worker.TransitionEffectWorker
import java.util.concurrent.LinkedBlockingQueue

class EffectDispatcher {
    private val eventQueue: LinkedBlockingQueue<Event>
    private val effectQueue: LinkedBlockingQueue<EffectInvocation>

    private val transitionConsumerThread: Thread

    constructor(eventQueue: LinkedBlockingQueue<Event>, effectQueue: LinkedBlockingQueue<EffectInvocation>) {
        this.eventQueue = eventQueue
        this.effectQueue = effectQueue
        val transitionEffectWorker: TransitionEffectWorker = TransitionEffectWorker(eventQueue, effectQueue)
        this.transitionConsumerThread = Thread(transitionEffectWorker)

        transitionConsumerThread.name = "TransitionEffectConsumer Consumer Thread"
        transitionConsumerThread.isDaemon = true
        transitionConsumerThread.start()
    }
}
