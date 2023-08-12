package com.pubnub.api.eventengine

interface State<Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> {
    open fun onEntry(): Set<Ei> = setOf()
    open fun onExit(): Set<Ei> = setOf()
    abstract fun transition(event: Ev): Pair<S, Set<Ei>>
}

fun <Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> S.noTransition(): Pair<S, Set<Ei>> = Pair(this, emptySet())
fun <Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> S.transitionTo(
    state: S,
    vararg invocations: Ei
): Pair<S, Set<Ei>> {
    val effectInvocations = this.onExit() + invocations + state.onEntry()
    return Pair(state, effectInvocations)
}
