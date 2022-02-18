package com.pubnub.api.state

interface Event

interface State<E : Effect> {
    fun onEntry(): Collection<E>
    fun onExit(): Collection<E>
}
