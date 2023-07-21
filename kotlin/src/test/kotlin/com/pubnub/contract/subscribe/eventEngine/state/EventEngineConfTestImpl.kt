package com.pubnub.contract.subscribe.eventEngine.state

import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event

class EventEngineConfTestImpl(
    queuedElements: MutableList<Pair<String, String>>
) : EventEngineConf<Event, SubscribeEffectInvocation> {

    private val eventSinkSource: TestSinkSource<Event> = TestSinkSource(queuedElements, QueueSinkSource())
    private val effectSinkSource: TestSinkSource<SubscribeEffectInvocation> =
        TestSinkSource(queuedElements, QueueSinkSource())

    override val eventSink: Sink<Event> = eventSinkSource
    override val eventSource: Source<Event> = eventSinkSource
    override val effectSink: Sink<SubscribeEffectInvocation> = effectSinkSource
    override val effectSource: Source<SubscribeEffectInvocation> = effectSinkSource
}
