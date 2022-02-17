package com.pubnub.api.state

import java.util.*

interface Event

abstract class Effect(val id: String = UUID.randomUUID().toString()) {
    abstract val child: Effect?
}

interface State<E : Effect> {
    fun onEntry(): Collection<E>
    fun onExit(): Collection<E>
}
