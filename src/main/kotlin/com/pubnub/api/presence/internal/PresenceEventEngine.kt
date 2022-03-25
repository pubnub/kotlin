package com.pubnub.api.presence.internal

import com.pubnub.api.state.Engine
import com.pubnub.api.state.EngineImplementation
import com.pubnub.api.state.transitionTo

typealias PresenceStateMachine = Engine<PresenceState, PresenceEvent, PresenceEffectInvocation>

internal fun presenceEventEngine(): Pair<PresenceStateMachine, List<PresenceEffectInvocation>> {
    val engine = EngineImplementation(
        initialState = Unsubscribed,
        transition = presenceTransition()
    )

    val effects = engine.transition(InitialEvent)
    return engine to effects
}

internal fun defineTransition(transitionFn: PresenceTransitionContext.(PresenceState, PresenceEvent) -> Pair<PresenceState, List<PresenceEffectInvocation>>): PresenceTransition {
    return { s, i ->
        val context = PresenceTransitionContext(s, i)
        if (i is InitialEvent) {
            val (_, newEffects) = context.transitionTo(s)
            s to (s.onEntry() + newEffects)
        } else {
            val (newState, newEffects) = context.transitionFn(s, i)
            if (newEffects.any { it is NewState }) {
                newState to (s.onExit() + newEffects + newState.onEntry())
            } else {
                newState to newEffects
            }
        }
    }
}


