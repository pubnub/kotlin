package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State
import java.util.concurrent.LinkedBlockingQueue

class SubscribeManagerEE {
    private val eventEngine: EventEngine
    private val effectDispatcher: EffectDispatcher

    constructor(state: State) {
        val eventQueue: LinkedBlockingQueue<Event> = LinkedBlockingQueue<Event>()
        val effectQueue: LinkedBlockingQueue<EffectInvocation> = LinkedBlockingQueue<EffectInvocation>()

        this.eventEngine = EventEngine(state, eventQueue, effectQueue)
        this.effectDispatcher = EffectDispatcher(eventQueue, effectQueue)
    }

    fun handleEvent(event: Event) {
        eventEngine.performTransitionEmitEffects(event)
    }
}
