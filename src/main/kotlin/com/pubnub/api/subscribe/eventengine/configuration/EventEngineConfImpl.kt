package com.pubnub.api.subscribe.eventengine.configuration

import com.pubnub.api.eventengine.EffectSink
import com.pubnub.api.eventengine.EffectSource
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.EventSource
import com.pubnub.api.subscribe.eventengine.effect.EffectSinkImpl
import com.pubnub.api.subscribe.eventengine.effect.EffectSourceImpl
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.EventSinkImpl
import com.pubnub.api.subscribe.eventengine.event.EventSourceImpl
import java.util.concurrent.LinkedBlockingQueue

class EventEngineConfImpl : EventEngineConf {
    private val eventQueue: LinkedBlockingQueue<Event> = LinkedBlockingQueue<Event>()
    private val invocationQueue: LinkedBlockingQueue<SubscribeEffectInvocation> = LinkedBlockingQueue<SubscribeEffectInvocation>()

    override fun getEventSink(): EventSink = EventSinkImpl(eventQueue)
    override fun getEventSource(): EventSource = EventSourceImpl(eventQueue)

    override fun getEffectSink(): EffectSink<SubscribeEffectInvocation> = EffectSinkImpl(invocationQueue)
    override fun getEffectSource(): EffectSource<SubscribeEffectInvocation> = EffectSourceImpl(invocationQueue)
}
