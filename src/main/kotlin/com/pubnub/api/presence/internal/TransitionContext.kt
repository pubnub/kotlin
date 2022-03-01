package com.pubnub.api.presence.internal

import com.pubnub.api.subscribe.internal.SubscriptionStatus
import com.pubnub.api.subscribe.internal.plus


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


internal fun TransitionContext.noTransition(): Pair<PresenceState, Collection<PresenceEffect>> {
    return state to listOf()
}

internal fun TransitionContext.transitionTo(
    target: PresenceState
): Pair<PresenceState, Collection<PresenceEffect>> {
    return target to listOf(NewState(target::class.simpleName!!))
}

internal fun TransitionContext.transitionTo(
    target: PresenceState,
    onExit: PresenceEffect
): Pair<PresenceState, Collection<PresenceEffect>> {
    return target to listOf(NewState(target::class.simpleName!!), onExit)
}

internal fun TransitionContext.transitionTo(
    target: PresenceState,
    onExit: Collection<PresenceEffect> = listOf()
): Pair<PresenceState, Collection<PresenceEffect>> {
    return target to listOf(NewState(target::class.simpleName!!)) + onExit
}

internal fun TransitionContext.cancel(vararg effects: PresenceEffect): Collection<CancelEffect> {
    return effects.flatMap { (it.child?.let { child -> cancel(child) } ?: listOf()) + listOf(CancelEffect(it.id)) }
}


