package com.pubnub.api.state

interface ExtendedState

interface State<E : EffectInvocation, ES : ExtendedState> {
    val extendedState: ES
    fun onEntry(): Collection<E>
    fun onExit(): Collection<E>
}
