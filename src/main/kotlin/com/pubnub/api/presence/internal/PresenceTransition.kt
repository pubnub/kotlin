package com.pubnub.api.presence.internal

import com.pubnub.api.state.Transition
import com.pubnub.api.state.noTransition
import com.pubnub.api.state.transitionTo

typealias PresenceTransition = Transition<PresenceState, PresenceEvent, PresenceEffectInvocation>

fun presenceTransition(): PresenceTransition = defineTransition { state, event ->
    when (state) {
        is Notify -> when (event) {
            is Commands.SubscribeIssued -> transitionTo(Notify(extendedState = updatedStatus))
            is Commands.UnsubscribeIssued -> transitionTo(
                target = Notify(extendedState = updatedStatus),
                onExit = IAmAwayEffectInvocation(channels = event.channels, channelGroups = event.groups)
            )
            is Commands.UnsubscribeAllIssued -> transitionTo(
                target = Unsubscribed,
                onExit = IAmAwayEffectInvocation(
                    channels = state.extendedState.channels,
                    channelGroups = state.extendedState.groups
                )

            )
            IAmHere.Succeed -> transitionTo(Waiting(extendedState = updatedStatus))
            else -> noTransition()
        }
        is Unsubscribed -> when (event) {
            is Commands.SubscribeIssued -> transitionTo(Notify(extendedState = updatedStatus))
            else -> noTransition()
        }
        is Waiting -> when (event) {
            HeartbeatIntervalOver -> transitionTo(Notify(updatedStatus))
            is Commands.SubscribeIssued -> transitionTo(
                target = Notify(updatedStatus),
                onExit = cancel(state.timer)
            )
            is Commands.UnsubscribeIssued -> transitionTo(
                target = Notify(updatedStatus),
                onExit = cancel(state.timer)
            )
            is Commands.UnsubscribeAllIssued -> transitionTo(
                target = Notify(updatedStatus),
                onExit = cancel(state.timer)
            )
            else -> noTransition()
        }
    }
}