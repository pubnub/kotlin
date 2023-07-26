package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

interface EventEngineConf { // todo introduce generic U, V for Event and EffectInvocation
    val subscribeEventSink: Sink<SubscribeEvent> // todo maybe SubscribeEvent ->  Event
    val subscribeEventSource: Source<SubscribeEvent>

    val effectSink: Sink<SubscribeEffectInvocation> // todo maybe SubscribeEffectInvocation ->  EffectInvocation
    val effectSource: Source<SubscribeEffectInvocation>
}
