package com.pubnub.api.eventengine

internal interface EventEngineConf<Ei : EffectInvocation, Ev : Event> {
    val eventSink: Sink<Ev>
    val eventSource: Source<Ev>
    val effectSink: Sink<Ei>
    val effectSource: Source<Ei>
}

internal class QueueEventEngineConf<Ei : EffectInvocation, Ev : Event>(
    effectSinkSource: SinkSource<Ei> = QueueSinkSource(),
    eventSinkSource: SinkSource<Ev> = QueueSinkSource()
) : EventEngineConf<Ei, Ev> {
    override val eventSink: Sink<Ev> = eventSinkSource
    override val eventSource: Source<Ev> = eventSinkSource
    override val effectSink: Sink<Ei> = effectSinkSource
    override val effectSource: Source<Ei> = effectSinkSource
}
