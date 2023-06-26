package com.pubnub.api.subscribe.eventengine.configuration

import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.LinkedBlockingQueue

class EventEngineConfImpl : EventEngineConf {
    private val eventSinkSource = QueueSinkSource<Event>(LinkedBlockingQueue())
    private val effectSinkSource = QueueSinkSource<SubscribeEffectInvocation>(LinkedBlockingQueue())

    override fun getEventSink(): Sink<Event> = eventSinkSource
    override fun getEventSource(): Source<Event> = eventSinkSource

    override fun getEffectSink(): Sink<SubscribeEffectInvocation> = effectSinkSource
    override fun getEffectSource(): Source<SubscribeEffectInvocation> = effectSinkSource
}
