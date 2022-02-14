package com.pubnub.api.state

abstract class AbstractMachine<I : Input, E : Effect>(private var currentState: AbstractState<I, E>) {

    fun handle(input: I): Collection<E> {
        val (events, newState) = currentState.transition(input)
        currentState = newState
        return events
    }
}
