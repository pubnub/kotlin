package com.pubnub.api.state

import java.util.*

interface Input

abstract class Effect(val id: String = UUID.randomUUID().toString())



abstract class AbstractState<I : Input, out E : Effect>(private val newStateFactory: (input: I) -> Pair<Collection<E>, AbstractState<I, E>?>) {

    protected open fun onEntry(): Collection<E> = listOf()
    protected open fun onExit(): Collection<E> = listOf()

    fun transition(input: I): Pair<Collection<E>, AbstractState<I, E>> {
        val (effects, maybeNewState) = newStateFactory(input)

        return maybeNewState?.let { (effects + this.onExit() + it.onEntry()) to it }
            ?: (effects to this)
    }
}

