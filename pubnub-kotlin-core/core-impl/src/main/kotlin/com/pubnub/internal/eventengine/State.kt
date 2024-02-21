package com.pubnub.internal.eventengine

internal interface State<Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> {
    fun onEntry(): Set<Ei> = setOf()
    fun onExit(): Set<Ei> = setOf()
    fun transition(event: Ev): Pair<S, Set<Ei>>
}

internal fun <Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> S.noTransition(): Pair<S, Set<Ei>> =
    Pair(this, emptySet())

internal fun <Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> S.transitionTo(
    state: S,
    vararg invocations: Ei
): Pair<S, Set<Ei>> {
    val effectInvocations = this.onExit() + invocations + state.onEntry()
    return Pair(state, effectInvocations)
}
