package com.pubnub.api.presence.internal

import com.pubnub.api.state.TransitionContext

internal class PresenceTransitionContext(presenceState: PresenceState, presenceEvent: PresenceEvent) : TransitionContext<PresenceEffectInvocation, PresenceExtendedState, PresenceEvent, PresenceState>(presenceState, presenceEvent) {
    override val updatedExtendedState: PresenceExtendedState = presenceState.extendedState + presenceEvent
}

internal operator fun PresenceExtendedState.plus(event: PresenceEvent): PresenceExtendedState {
    return when (event) {
        is Commands.SubscribeIssued -> copy(
            channels = channels + event.channels.toSet(),
            groups = groups + event.groups.toSet(),
        )
        Commands.UnsubscribeAllIssued -> PresenceExtendedState()
        is Commands.UnsubscribeIssued -> copy(
            groups = groups - event.groups.toSet(),
        )
        else -> this
    }
}

internal fun PresenceTransitionContext.cancel(vararg effects: PresenceEffectInvocation): List<PresenceEffectInvocation> {
    return effects.flatMap { listOf(CancelEffectInvocation(it.id())) }
}
