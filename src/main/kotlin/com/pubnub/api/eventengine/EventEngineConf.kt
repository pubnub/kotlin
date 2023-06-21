package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation

interface EventEngineConf {
    fun getEventSink(): EventSink
    fun getEventSource(): EventSource

    fun getEffectSink(): EffectSink<SubscribeEffectInvocation>
    fun getEffectSource(): EffectSource<SubscribeEffectInvocation>
}
