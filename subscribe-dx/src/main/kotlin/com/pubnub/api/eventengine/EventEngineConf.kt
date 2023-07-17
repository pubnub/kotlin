package com.pubnub.api.eventengine

interface EventEngineConf<Ev : Event, Ef : EffectInvocation> {
    val eventSink: Sink<Ev>
    val eventSource: Source<Ev>

    val effectSink: Sink<Ef>
    val effectSource: Source<Ef>
}
