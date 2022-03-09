package com.pubnub.api.presence.internal

import com.pubnub.api.state.QueuedEventEngine
import com.pubnub.api.state.EventEngine
import com.pubnub.api.state.Transition
import java.util.concurrent.LinkedBlockingQueue

typealias PresenceTransition = Transition<PresenceState, PresenceEvent, PresenceEffect>

typealias PresenceStateMachine = EventEngine<PresenceState, PresenceEvent, PresenceEffect>

typealias QueuedPresenceEventEngine = QueuedEventEngine<PresenceState, PresenceEvent, PresenceEffect>

fun presenceEventEngine(): Pair<PresenceStateMachine, Collection<PresenceEffect>> {
    return EventEngine.create(
        initialState = Unsubscribed,
        transition = presenceTransition(),
        initialEvent = InitialEvent
    )
}

fun queuedPresenceEventEngine(
    eventQueue: LinkedBlockingQueue<PresenceEvent>,
    effectQueue: LinkedBlockingQueue<PresenceEffect>
): QueuedPresenceEventEngine {
    val (stateMachine, initialEffects) = presenceEventEngine()
    return QueuedPresenceEventEngine.create(
        eventEngine = stateMachine,
        initialEffects = initialEffects,
        eventQueue = eventQueue,
        effectQueue = effectQueue
    )
}

internal fun defineTransition(transitionFn: TransitionContext.(PresenceState, PresenceEvent) -> Pair<PresenceState, Collection<PresenceEffect>>): PresenceTransition {
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


fun presenceTransition(): PresenceTransition = defineTransition { state, event ->
    when (state) {
        is Notify -> when (event) {
            is Commands.SubscribeIssued -> transitionTo(Notify(status = updatedStatus))
            is Commands.UnsubscribeIssued -> transitionTo(
                Notify(status = updatedStatus),
                IAmAwayEffect(channels = event.channels, channelGroups = event.groups)
            )
            is Commands.UnsubscribeAllIssued -> transitionTo(
                Unsubscribed,
                IAmAwayEffect(channels = state.status.channels, channelGroups = state.status.groups)

            )
            IAmHere.Succeed -> transitionTo(Waiting(status = updatedStatus))
            else -> noTransition()
        }
        Unsubscribed -> when (event) {
            is Commands.SubscribeIssued -> transitionTo(Notify(status = updatedStatus))
            else -> noTransition()
        }
        is Waiting -> when (event) {
            HeartbeatIntervalOver -> transitionTo(Notify(updatedStatus))
            is Commands.SubscribeIssued -> transitionTo(Notify(updatedStatus), cancel(state.timer))
            is Commands.UnsubscribeIssued -> transitionTo(Notify(updatedStatus), cancel(state.timer))
            is Commands.UnsubscribeAllIssued -> transitionTo(Notify(updatedStatus), cancel(state.timer))
            else -> noTransition()
        }
    }
}