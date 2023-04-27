package com.pubnub.api.eventengine

interface State<T : EffectInvocation, U : Event, V : State<T, U, V>> {
    open fun onEntry(): List<T> = listOf()
    open fun onExit(): List<T> = listOf()
    abstract fun transition(event: U): Pair<V, List<T>>
}
