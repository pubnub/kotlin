package com.pubnub.api.state

import java.util.*

interface Input

abstract class Effect(val id: String = UUID.randomUUID().toString())

data class NewStateEffect(val name: String) : Effect()

abstract class AbstractState<I : Input>(private val newState: (input: I) -> Pair<Collection<Effect>, AbstractState<I>?>) {

    protected open fun onEntry(): Collection<Effect> = listOf()
    protected open fun onExit(): Collection<Effect> = listOf()

    fun transition(input: I): Pair<Collection<Effect>, AbstractState<I>> {
        val (effects, maybeNewState) = newState(input)

        return maybeNewState?.let { (effects + this.onExit() + listOf(NewStateEffect(it::class.java.name)) + it.onEntry()) to it } ?: (effects to this)
    }
}

