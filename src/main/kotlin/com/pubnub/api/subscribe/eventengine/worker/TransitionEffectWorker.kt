package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.LinkedBlockingQueue

class TransitionEffectWorker : Runnable {

    private val eventQueue: LinkedBlockingQueue<Event>
    private val effectQueue: LinkedBlockingQueue<EffectInvocation>

    constructor(eventQueue: LinkedBlockingQueue<Event>, effectQueue: LinkedBlockingQueue<EffectInvocation>) {
        this.eventQueue = eventQueue
        this.effectQueue = effectQueue
    }

    override fun run() {
        TODO("Not yet implemented")
    }
}
