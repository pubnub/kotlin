package com.pubnub.api.state

import java.util.concurrent.atomic.AtomicReference

interface Engine<S, EV, EF> {
    fun currentState(): S
    fun transition(event: EV): List<EF>
}

class EngineImplementation<S, EV, EF>(
    initialState: S,
    private val transition: Transition<S, EV, EF>,
) : Engine<S, EV, EF> {
    private var state: AtomicReference<S> = AtomicReference(initialState)
    override fun currentState(): S = state.get()

    override fun transition(event: EV): List<EF> {
        val (newState, effects) = transition(state.get(), event)
        state.set(newState)
        return effects
    }
}
