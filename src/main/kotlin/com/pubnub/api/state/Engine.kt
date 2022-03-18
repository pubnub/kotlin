package com.pubnub.api.state

interface Engine<S, EV, EF> {
    fun currentState(): S
    fun transition(event: EV): List<EF>
}
