package com.pubnub.api.managers

import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

class EventEngineManager(
    private val subscribeEventEngine: SubscribeEventEngine,
    private val effectDispatcher: EffectDispatcher<SubscribeEffectInvocation>,
    private val eventSink: Sink<SubscribeEvent>
) {

    fun addEventToQueue(subscribeEvent: SubscribeEvent) {
        eventSink.add(subscribeEvent) // todo add unit tests
    }

    fun start() {
        subscribeEventEngine.start()
        effectDispatcher.start()
    }

    fun stop() {
        subscribeEventEngine.stop()
        effectDispatcher.stop()
    }
}
