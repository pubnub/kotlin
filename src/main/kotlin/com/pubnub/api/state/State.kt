package com.pubnub.api.state

import java.util.*

interface Event

abstract class Effect(val id: String = UUID.randomUUID().toString())

abstract class AbstractState<in Ev : Event, out Ef : Effect>(private val newStateFactory: (input: Ev) -> Pair<Collection<Ef>, AbstractState<Ev, Ef>?>) {

    protected open fun onEntry(): Collection<Ef> = listOf()
    protected open fun onExit(): Collection<Ef> = listOf()

    fun transition(event: Ev): Pair<Collection<Ef>, AbstractState<Ev, Ef>> {
        val (effects, maybeNewState) = newStateFactory(event)

        return maybeNewState?.let { (effects + this.onExit() + it.onEntry()) to it }
            ?: (effects to this)
    }
}

