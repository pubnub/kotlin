package com.pubnub.api.presence.internal

import com.pubnub.api.state.Transition

typealias PresenceTransition = Transition<PresenceState, PresenceEvent, PresenceEffect>

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