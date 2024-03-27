package com.pubnub.internal.eventengine

internal class EventEngineManager<Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>, Ee : EventEngine<Ei, Ev, S>>(
    private val eventEngine: Ee,
    private val effectDispatcher: EffectDispatcher<Ei>,
    private val eventSink: Sink<Ev>,
) {
    fun addEventToQueue(event: Ev) {
        eventSink.add(event)
    }

    fun start() {
        eventEngine.start()
        effectDispatcher.start()
    }

    fun stop() {
        eventEngine.stop()
        effectDispatcher.stop()
    }
}
