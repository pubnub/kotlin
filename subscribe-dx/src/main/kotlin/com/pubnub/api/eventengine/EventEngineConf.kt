package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation

interface EventEngineConf {
    val eventSink: Sink<com.pubnub.api.eventengine.Event>
    val eventSource: Source<com.pubnub.api.eventengine.Event>

    val effectSink: Sink<SubscribeEffectInvocation>
    val effectSource: Source<SubscribeEffectInvocation>
}
