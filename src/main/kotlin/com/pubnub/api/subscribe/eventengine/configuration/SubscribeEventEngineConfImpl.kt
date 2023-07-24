package com.pubnub.api.subscribe.eventengine.configuration

import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

class SubscribeEventEngineConfImpl : EventEngineConf {
    private val subscribeEventSinkSourceQueue = QueueSinkSource<SubscribeEvent>()
    private val effectSinkSourceQueue = QueueSinkSource<SubscribeEffectInvocation>()

    override val subscribeEventSink: Sink<SubscribeEvent> = subscribeEventSinkSourceQueue
    override val subscribeEventSource: Source<SubscribeEvent> = subscribeEventSinkSourceQueue

    override val effectSink: Sink<SubscribeEffectInvocation> = effectSinkSourceQueue
    override val effectSource: Source<SubscribeEffectInvocation> = effectSinkSourceQueue
}
