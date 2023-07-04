package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event

interface EventEngineConf {
    val eventSink: Sink<Event>
    val eventSource: Source<Event>

    val effectSink: Sink<SubscribeEffectInvocation>
    val effectSource: Source<SubscribeEffectInvocation>
}
