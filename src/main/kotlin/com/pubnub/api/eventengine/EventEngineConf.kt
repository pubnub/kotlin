package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event

interface EventEngineConf {
    fun getEventSink(): Sink<Event>
    fun getEventSource(): Source<Event>

    fun getEffectSink(): Sink<SubscribeEffectInvocation>
    fun getEffectSource(): Source<SubscribeEffectInvocation>
}
