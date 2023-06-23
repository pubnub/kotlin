package com.pubnub.contract.state

import com.pubnub.api.eventengine.EffectSink
import com.pubnub.api.eventengine.EffectSource
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.EventSource
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event

class EventEngineConfTestImpl(
    queuedElements: MutableList<Pair<String, String>>
) : EventEngineConf {

    private val eventSinkSource: TestSinkSource<Event> = TestSinkSource(queuedElements, QueueSinkSource())
    private val effectSinkSource: TestSinkSource<SubscribeEffectInvocation> = TestSinkSource(queuedElements, QueueSinkSource())

    override fun getEventSink(): EventSink = eventSinkSource

    override fun getEventSource(): EventSource = eventSinkSource

    override fun getEffectSink(): EffectSink<SubscribeEffectInvocation> = effectSinkSource

    override fun getEffectSource(): EffectSource<SubscribeEffectInvocation> = effectSinkSource
}
