package com.pubnub.api.presence.internal

import com.pubnub.api.state.internal.QueuedEventEngine
import com.pubnub.api.state.internal.EventEngine
import java.util.concurrent.LinkedBlockingQueue



typealias PresenceStateMachine = EventEngine<PresenceState, PresenceEvent, PresenceEffectInvocation>

typealias QueuedPresenceEventEngine = QueuedEventEngine<PresenceState, PresenceEvent, PresenceEffectInvocation>

fun presenceEventEngine(): Pair<PresenceStateMachine, Collection<PresenceEffectInvocation>> {
    return EventEngine.create(
        initialState = Unsubscribed,
        transition = presenceTransition(),
        initialEvent = InitialEvent
    )
}

fun queuedPresenceEventEngine(
    eventQueue: LinkedBlockingQueue<PresenceEvent>,
    effectQueue: LinkedBlockingQueue<PresenceEffectInvocation>
): QueuedPresenceEventEngine {
    val (stateMachine, initialEffects) = presenceEventEngine()
    return QueuedPresenceEventEngine.create(
        eventEngine = stateMachine,
        initialEffects = initialEffects,
        eventQueue = eventQueue,
        effectQueue = effectQueue
    )
}

internal fun defineTransition(transitionFn: TransitionContext.(PresenceState, PresenceEvent) -> Pair<PresenceState, Collection<PresenceEffectInvocation>>): PresenceTransition {
    return { s, i ->
        val context = TransitionContext(s, i)
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


