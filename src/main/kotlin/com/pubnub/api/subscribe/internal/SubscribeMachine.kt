package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.StateMachine
import com.pubnub.api.state.Transition
import com.pubnub.api.subscribe.internal.Commands.*
import com.pubnub.api.subscribe.internal.HandshakeResult.*
import com.pubnub.api.subscribe.internal.ReceivingResult.*

typealias SubscribeTransition = Transition<SubscribeState, SubscribeEvent, SubscribeEffect>

typealias SubscribeMachine = StateMachine<SubscribeEvent, SubscribeEffect>

/**
 * Use from single thread only
 */
fun subscribeMachine(
    shouldRetry: (Int) -> Boolean,
    transitions: SubscribeTransition = subscribeTransition(shouldRetry),
    initState: SubscribeState = Unsubscribed,
): SubscribeMachine {
    var state = initState
    return { event ->
        val (ns, effects) = transitions(state, event)
        state = ns
        effects
    }
}


internal fun defineTransition(transitionFn: TransitionContext.(SubscribeState, SubscribeEvent) -> Pair<SubscribeState, Collection<SubscribeEffect>>): SubscribeTransition {
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

internal fun subscribeTransition(shouldRetry: (Int) -> Boolean): SubscribeTransition =
    defineTransition { state, event ->
        when (state) {
            is Handshaking -> when (event) {
                is SubscribeIssued -> transitionTo(
                    target = Handshaking(updatedStatus),
                    onExit = cancel(state.call)
                )
                UnsubscribeAllIssued -> transitionTo(
                    target = Unsubscribed,
                    onExit = cancel(state.call)
                )
                is UnsubscribeIssued -> transitionTo(
                    target = Handshaking(updatedStatus),
                    onExit = cancel(state.call)
                )
                is HandshakeSucceeded -> transitionTo(Receiving(updatedStatus))
                is HandshakeFailed -> if (shouldRetry(state.retryCount)) {
                    transitionTo(state.copy(retryCount = state.retryCount + 1))
                } else {
                    transitionTo(HandshakingFailed(updatedStatus))
                }
                else -> noTransition()
            }
            is Receiving -> {
                when (event) {
                    is SubscribeIssued -> transitionTo(
                        target = Receiving(updatedStatus),
                        onExit = cancel(state.call)
                    )
                    UnsubscribeAllIssued -> transitionTo(
                        target = Unsubscribed,
                        onExit = cancel(state.call)
                    )
                    is UnsubscribeIssued -> transitionTo(
                        target = Receiving(updatedStatus),
                        onExit = cancel(state.call)
                    )

                    ReceivingFailed -> transitionTo(Reconnecting(updatedStatus))
                    is ReceivingSucceeded -> transitionTo(
                        target = Receiving(updatedStatus),
                        onExit = NewMessages(event.subscribeEnvelope.messages)
                    )
                    else -> noTransition()
                }
            }
            Unsubscribed -> when (event) {
                is SubscribeIssued -> transitionTo(Handshaking(updatedStatus))
                else -> noTransition()
            }
            is HandshakingFailed -> when (event) {
                is SubscribeIssued -> transitionTo(Handshaking(updatedStatus))
                UnsubscribeAllIssued -> transitionTo(Unsubscribed)
                is UnsubscribeIssued -> transitionTo(Handshaking(updatedStatus))
                else -> noTransition()
            }
            is Reconnecting ->
                when (event) {
                    is SubscribeIssued -> transitionTo(
                        target = Reconnecting(updatedStatus),
                        onExit = cancel(state.call)
                    )
                    UnsubscribeAllIssued -> transitionTo(
                        target = Unsubscribed,
                        onExit = cancel(state.call)
                    )
                    is UnsubscribeIssued -> transitionTo(
                        target = Reconnecting(updatedStatus),
                        onExit = cancel(state.call)
                    )

                    is ReceivingSucceeded -> transitionTo(
                        target = Receiving(updatedStatus),
                        onExit = NewMessages(event.subscribeEnvelope.messages)
                    )
                    is ReceivingFailed -> if (shouldRetry(state.retryCount)) {
                        transitionTo(state.copy(retryCount = state.retryCount + 1))
                    } else {
                        transitionTo(ReconnectingFailed(updatedStatus))
                    }
                    else -> noTransition()

                }
            is ReconnectingFailed -> when (event) {
                else -> noTransition() //TODO figure out transitions out of it
            }
        }
    }
