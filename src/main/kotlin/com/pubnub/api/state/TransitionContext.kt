package com.pubnub.api.state

@DslMarker
annotation class StateMachineContext

@StateMachineContext
internal abstract class TransitionContext<EF : EffectInvocation, EX : ExtendedState, EV : Event, S : State<EF, EX>>(
    val state: S,
    val event: EV
) {
    abstract val updatedStatus: EX
}


internal fun <EF : EffectInvocation, EX : ExtendedState, EV : Event, S : State<EF, EX>> TransitionContext<EF, EX, EV, S>.noTransition(): Pair<S, List<EF>> {
    return state to listOf()
}

internal fun <EF : EffectInvocation, EX : ExtendedState, EV : Event, S : State<EF, EX>> TransitionContext<EF, EX, EV, S>.transitionTo(
    target: S
): Pair<S, List<EF>> {
    return target to listOf()
}

internal fun <EF : EffectInvocation, EX : ExtendedState, EV : Event, S : State<EF, EX>> TransitionContext<EF, EX, EV, S>.transitionTo(
    target: S,
    onExit: EF
): Pair<S, List<EF>> {
    return target to listOf(onExit)
}

internal fun <EF : EffectInvocation, EX : ExtendedState, EV : Event, S : State<EF, EX>> TransitionContext<EF, EX, EV, S>.transitionTo(
    target: S,
    onExit: List<EF> = listOf()
): Pair<S, List<EF>> {
    return target to onExit
}

