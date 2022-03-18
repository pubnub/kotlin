package com.pubnub.api.presence.internal

@DslMarker
annotation class StateMachineContext

@StateMachineContext
internal data class TransitionContext(
    val state: PresenceState,
    val event: PresenceEvent
) {
    val updatedStatus: PresenceStatus = state.status + event
}

internal operator fun PresenceStatus.plus(event: PresenceEvent): PresenceStatus {
    return when (event) {
        is Commands.SubscribeIssued -> copy(
            channels = channels + event.channels.toSet(),
            groups = groups + event.groups.toSet(),
        )
        Commands.UnsubscribeAllIssued -> PresenceStatus()
        is Commands.UnsubscribeIssued -> copy(
            groups = groups - event.groups.toSet(),
        )
        else -> this
    }
}


internal fun TransitionContext.noTransition(): Pair<PresenceState, Collection<PresenceEffectInvocation>> {
    return state to listOf()
}

internal fun TransitionContext.transitionTo(
    target: PresenceState
): Pair<PresenceState, Collection<PresenceEffectInvocation>> {
    return target to listOf(NewState(target::class.simpleName!!))
}

internal fun TransitionContext.transitionTo(
    target: PresenceState,
    onExit: PresenceEffectInvocation
): Pair<PresenceState, Collection<PresenceEffectInvocation>> {
    return target to listOf(NewState(target::class.simpleName!!), onExit)
}

internal fun TransitionContext.transitionTo(
    target: PresenceState,
    onExit: Collection<PresenceEffectInvocation> = listOf()
): Pair<PresenceState, Collection<PresenceEffectInvocation>> {
    return target to listOf(NewState(target::class.simpleName!!)) + onExit
}

internal fun TransitionContext.cancel(vararg effects: PresenceEffectInvocation): Collection<CancelEffectInvocation> {
    return effects.flatMap { (it.child?.let { child -> cancel(child) } ?: listOf()) + listOf(CancelEffectInvocation(it.id)) }
}


