package com.pubnub.internal.eventengine

import org.slf4j.LoggerFactory

internal interface State<Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> {
    fun onEntry(): Set<Ei> = setOf()

    fun onExit(): Set<Ei> = setOf()

    fun transition(event: Ev): Pair<S, Set<Ei>>
}

internal fun <Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> S.noTransition(): Pair<S, Set<Ei>> = Pair(this, emptySet())

internal fun <Ei : EffectInvocation, Ev : Event, S : State<Ei, Ev, S>> S.transitionTo(
    state: S,
    vararg invocations: Ei,
): Pair<S, Set<Ei>> {
    val logger = LoggerFactory.getLogger(this::class.java)
    logger.trace("Transitioning from ${this::class.simpleName} to ${state::class.simpleName} with ${invocations.size} " +
            "invocations: ${invocations.joinToString(", ")}")

    val effectInvocations = this.onExit() + invocations + state.onEntry()
    return Pair(state, effectInvocations)
}
