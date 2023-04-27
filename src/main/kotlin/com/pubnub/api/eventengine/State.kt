package com.pubnub.api.eventengine

interface State<T : EffectInvocation, U : Event, V : State<T, U, V>> {
    open fun onEntry(): List<T> = listOf()
    open fun onExit(): List<T> = listOf()
    abstract fun transition(event: U): Pair<V, List<T>>
}

fun <T : EffectInvocation, U : Event, V : State<T, U, V>> V.noTransition(): Pair<V, List<T>> = Pair(this, emptyList())
fun <T : EffectInvocation, U : Event, V : State<T, U, V>> V.transitionTo(
    state: V,
    vararg invocations: T
): Pair<V, List<T>> {
    val effectInvocations = this.onExit() + invocations + state.onEntry()
    return Pair(state, effectInvocations)
}
