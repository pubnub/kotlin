package com.pubnub.api.subscribe.eventengine.configuration

import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event

class EventEngineConfImpl : EventEngineConf<Event, SubscribeEffectInvocation> {
    private val eventSinkSourceQueue = QueueSinkSource<Event>()
    private val effectSinkSourceQueue = QueueSinkSource<SubscribeEffectInvocation>()

    override val eventSink: Sink<Event> = eventSinkSourceQueue
    override val eventSource: Source<Event> = eventSinkSourceQueue

    override val effectSink: Sink<SubscribeEffectInvocation> = effectSinkSourceQueue
    override val effectSource: Source<SubscribeEffectInvocation> = effectSinkSourceQueue
}
