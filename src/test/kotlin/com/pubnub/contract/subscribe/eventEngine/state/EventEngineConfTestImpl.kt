package com.pubnub.contract.subscribe.eventEngine.state

import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

class EventEngineConfTestImpl(
    queuedElements: MutableList<Pair<String, String>>
) : EventEngineConf {

    private val subscribeEventSinkSource: TestSinkSource<SubscribeEvent> = TestSinkSource(queuedElements, QueueSinkSource())
    private val effectSinkSource: TestSinkSource<SubscribeEffectInvocation> =
        TestSinkSource(queuedElements, QueueSinkSource())

    override val subscribeEventSink: Sink<SubscribeEvent> = subscribeEventSinkSource
    override val subscribeEventSource: Source<SubscribeEvent> = subscribeEventSinkSource
    override val effectSink: Sink<SubscribeEffectInvocation> = effectSinkSource
    override val effectSource: Source<SubscribeEffectInvocation> = effectSinkSource
}
