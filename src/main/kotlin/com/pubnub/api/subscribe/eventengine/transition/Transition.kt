package com.pubnub.api.subscribe.eventengine.transition

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State

//cos jak statyczna metoda
internal fun transition(state: State, event: Event): Pair<State, List<EffectInvocation>> {
    val transitionResult = when (state) {
        is State.Unsubscribed -> {
//            state.transition(event)
            (State.Unsubscribed).transition(event)  //<-- może lepiej tak, żeby przekierowywało do odpowiendiej implementacji metody transition  ?
        }
        is State.Handshaking -> {
            state.transition(event)
//            (State.Handshaking).transition(event)   //<--dlaczego tu nie mogę tak?
        }
        is State.Receiving -> {
            state.transition(event)
//            (State.Receiving).transition(event)    //<--dlaczego tu nie mogę tak?
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

        else -> {
            TODO()
        }
    }

    val newState = transitionResult.first
    val transitionEffects = transitionResult.second


    val effectsOnCurrentStateExit = state.onExit()
    val effectsOnNewStateEntry = newState.onEntry()
    return newState to effectsOnCurrentStateExit + transitionEffects + effectsOnNewStateEntry
}