package com.pubnub.api.subscribe.eventengine.transition

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State

internal fun transition(state: State, event: Event): Pair<State, List<EffectInvocation>> {
    val transitionResult = when (state) {
        is State.Unsubscribed -> {
//            state.transition(event)
            (State.Unsubscribed).transition(event) // <-- może lepiej tak, żeby przekierowywało do odpowiendiej implementacji metody transition  ?
        }
        is State.Handshaking -> {
            state.transition(event)
        }
        is State.Receiving -> {
            state.transition(event)
        }
        is State.HandshakeReconnecting -> {
            state.transition(event)
        }
        is State.HandshakeFailed -> {
            state.transition(event)
        }
        is State.HandshakeStopped -> {
            state.transition(event)
        }
        is State.ReceiveReconnecting -> {
            state.transition(event)
        }
        is State.ReceiveFailed -> {
            state.transition(event)
        }
        is State.ReceiveStopped -> {
            state.transition(event)
        }
    }

    val newState = transitionResult.first
    val transitionEffects = transitionResult.second

    val effectsOnCurrentStateExit = state.onExit()
    val effectsOnNewStateEntry = newState.onEntry()
    return newState to effectsOnCurrentStateExit + transitionEffects + effectsOnNewStateEntry
}
