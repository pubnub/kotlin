package com.pubnub.contract.state

import com.pubnub.api.eventengine.EffectSink
import com.pubnub.api.eventengine.EffectSource
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.EventSource
import com.pubnub.api.subscribe.eventengine.effect.EffectSourceImpl
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.EventSourceImpl
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingQueue

class EventEngineConfTestImpl(
    private val happenings: CopyOnWriteArrayList<HashMap<String, String>>,
) : EventEngineConf {
    private val eventQueue: LinkedBlockingQueue<Event> = LinkedBlockingQueue<Event>()
    private val invocationQueue: LinkedBlockingQueue<SubscribeEffectInvocation> =
        LinkedBlockingQueue<SubscribeEffectInvocation>()

    override fun getEventSink(): EventSink {
        return EventSinkTestImpl(eventQueue, happenings)
    }

    override fun getEventSource(): EventSource {
        return EventSourceImpl(eventQueue)
    }

    override fun getEffectSink(): EffectSink<SubscribeEffectInvocation> {
        return EffectSinkTestImpl(invocationQueue, happenings)
    }

    override fun getEffectSource(): EffectSource<SubscribeEffectInvocation> {
        return EffectSourceImpl(invocationQueue)
    }
}
