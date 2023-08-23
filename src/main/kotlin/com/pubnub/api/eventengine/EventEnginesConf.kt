package com.pubnub.api.eventengine

interface EventEngineConf<Ei : EffectInvocation, Ev : Event> {
    val eventSink: Sink<Ev>
    val eventSource: Source<Ev>
    val effectSink: Sink<Ei>
    val effectSource: Source<Ei>
}

class QueueEventEngineConf<Ei : EffectInvocation, Ev : Event>(
    queueEffectSinkSource: SinkSource<Ei> = QueueSinkSource(),
    queueEventSinkSource: SinkSource<Ev> = QueueSinkSource()
) : EventEngineConf<Ei, Ev> {
    override val eventSink: Sink<Ev> = queueEventSinkSource
    override val eventSource: Source<Ev> = queueEventSinkSource
    override val effectSink: Sink<Ei> = queueEffectSinkSource
    override val effectSource: Source<Ei> = queueEffectSinkSource
}
