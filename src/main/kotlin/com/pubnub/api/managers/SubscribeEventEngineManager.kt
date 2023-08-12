package com.pubnub.api.managers

import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.Event
import com.pubnub.api.eventengine.EventEngineManager
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

class SubscribeEventEngineManager(
    private val subscribeEventEngine: SubscribeEventEngine,
    private val effectDispatcher: EffectDispatcher<SubscribeEffectInvocation>,
    private val eventSink: Sink<SubscribeEvent>
) : EventEngineManager {

    override fun addEventToQueue(event: Event) {
        eventSink.add(event as SubscribeEvent)
    }

    override fun start() {
        subscribeEventEngine.start()
        effectDispatcher.start()
    }

    override fun stop() {
        subscribeEventEngine.stop()
        effectDispatcher.stop()
    }
}
