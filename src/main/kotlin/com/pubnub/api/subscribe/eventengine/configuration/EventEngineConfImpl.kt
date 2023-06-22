package com.pubnub.api.subscribe.eventengine.configuration

import com.pubnub.api.eventengine.EffectSink
import com.pubnub.api.eventengine.EffectSource
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.EventSource
import com.pubnub.api.eventengine.QueueSinkSource
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.LinkedBlockingQueue

class EventEngineConfImpl : EventEngineConf {
    private val eventSinkSource = QueueSinkSource<Event>(LinkedBlockingQueue())
    private val effectSinkSource = QueueSinkSource<SubscribeEffectInvocation>(LinkedBlockingQueue())

    override fun getEventSink(): EventSink = eventSinkSource
    override fun getEventSource(): EventSource = eventSinkSource

    override fun getEffectSink(): EffectSink<SubscribeEffectInvocation> = effectSinkSource
    override fun getEffectSource(): EffectSource<SubscribeEffectInvocation> = effectSinkSource
}
