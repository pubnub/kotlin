package com.pubnub.api.eventengine

interface State<T : EffectInvocation, U : Event, V : State<T, U, V>> {
    open fun onEntry(): Set<T> = setOf()
    open fun onExit(): Set<T> = setOf()
    abstract fun transition(event: U): Pair<V, Set<T>>
}

fun <T : EffectInvocation, U : Event, V : State<T, U, V>> V.noTransition(): Pair<V, Set<T>> = Pair(this, emptySet())
fun <T : EffectInvocation, U : Event, V : State<T, U, V>> V.transitionTo(
    state: V,
    vararg invocations: T
): Pair<V, Set<T>> {
    val effectInvocations = this.onExit() + invocations + state.onEntry()
    return Pair(state, effectInvocations)
}
