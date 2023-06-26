package com.pubnub.contract.state

import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event

class EventEngineConfTestImpl(
    queuedElements: MutableList<Pair<String, String>>
) : EventEngineConf {

    private val eventSinkSource: TestSinkSource<Event> = TestSinkSource(queuedElements, QueueSinkSource())
    private val effectSinkSource: TestSinkSource<SubscribeEffectInvocation> =
        TestSinkSource(queuedElements, QueueSinkSource())

    override fun getEventSink(): Sink<Event> = eventSinkSource

    override fun getEventSource(): Source<Event> = eventSinkSource

    override fun getEffectSink(): Sink<SubscribeEffectInvocation> = effectSinkSource

    override fun getEffectSource(): Source<SubscribeEffectInvocation> = effectSinkSource
}
