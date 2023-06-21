package com.pubnub.api.managers

import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EffectFactory
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import java.util.concurrent.ConcurrentHashMap

class EventEngineManager(
    currenState: SubscribeState = SubscribeState.Unsubscribed,
    eventEngineConf: EventEngineConf,
    effectFactory: EffectFactory<SubscribeEffectInvocation>,
    managedEffects: ConcurrentHashMap<String, ManagedEffect> = ConcurrentHashMap(),
) {
    private val eventSink = eventEngineConf.getEventSink()
    private val subscribeEventEngine =
        SubscribeEventEngine(eventEngineConf.getEffectSink(), eventEngineConf.getEventSource(), currenState)
    private val effectDispatcher: EffectDispatcher<SubscribeEffectInvocation> =
        EffectDispatcher(effectFactory, managedEffects, eventEngineConf.getEffectSource())

    fun addEventToQueue(event: Event) {
        start() // todo add unit tests
        eventSink.add(event)
    }

    private fun start() {
        if (!subscribeEventEngine.isStarted) {
            subscribeEventEngine.start()
        }
        if (!effectDispatcher.isStarted) {
            effectDispatcher.start()
        }
    }
}
