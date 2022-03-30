package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.Transition
import com.pubnub.api.state.noTransition
import com.pubnub.api.state.transitionTo

typealias SubscribeTransition = Transition<SubscribeState, SubscribeEvent, SubscribeEffectInvocation>

internal fun subscribeTransition(retryPolicy: RetryPolicy): SubscribeTransition =
    defineTransition { state, event ->
        when (state) {
            is Handshaking -> when (event) {
                is Commands.SubscribeIssued -> transitionTo(
                    target = Handshaking(updatedStatus),
                    withEffects = cancel(state.call)
                )
                Commands.UnsubscribeAllIssued -> transitionTo(
                    target = Unsubscribed,
                    withEffects = cancel(state.call)
                )
                is Commands.UnsubscribeIssued -> transitionTo(
                    target = Handshaking(updatedStatus),
                    withEffects = cancel(state.call)
                )
                is HandshakeResult.HandshakeSucceeded -> transitionTo(
                    target = Receiving(updatedStatus),
                    withEffects = Connected
                )
                is HandshakeResult.HandshakeFailed -> if (retryPolicy.shouldRetry(state.extendedState.retryCounter)) {
                    transitionTo(state.copy(extendedState = state.extendedState.copy(retryCounter = state.extendedState.retryCounter + 1)))
                } else {
                    transitionTo(HandshakingFailed(updatedStatus))
                }
                else -> noTransition()
            }
            is Receiving -> {
                when (event) {
                    is Commands.SubscribeIssued -> transitionTo(
                        target = Receiving(updatedStatus),
                        withEffects = cancel(state.call)
                    )
                    Commands.UnsubscribeAllIssued -> transitionTo(
                        target = Unsubscribed,
                        withEffects = cancel(state.call) + Disconnected("Unsubscribed")
                    )
                    is Commands.UnsubscribeIssued -> transitionTo(
                        target = Receiving(updatedStatus),
                        withEffects = cancel(state.call)
                    )

                    is ReceivingResult.ReceivingFailed -> transitionTo(Reconnecting(updatedStatus))
                    is ReceivingResult.ReceivingSucceeded -> transitionTo(
                        target = Receiving(updatedStatus),
                        withEffects = NewMessages(event.subscribeEnvelope.messages)
                    )
                    else -> noTransition()
                }
            }
            Unsubscribed -> when (event) {
                is Commands.SubscribeIssued -> transitionTo(Handshaking(updatedStatus))
                else -> noTransition()
            }
            is HandshakingFailed -> when (event) {
                is Commands.SubscribeIssued -> transitionTo(Handshaking(updatedStatus))
                Commands.UnsubscribeAllIssued -> transitionTo(Unsubscribed)
                is Commands.UnsubscribeIssued -> transitionTo(Handshaking(updatedStatus))
                else -> noTransition()
            }
            is Reconnecting ->
                when (event) {
                    is Commands.SubscribeIssued -> transitionTo(
                        target = Reconnecting(updatedStatus),
                        withEffects = cancel(state.call)
                    )
                    Commands.UnsubscribeAllIssued -> transitionTo(
                        target = Unsubscribed,
                        withEffects = cancel(state.call)
                    )
                    is Commands.UnsubscribeIssued -> transitionTo(
                        target = Reconnecting(updatedStatus),
                        withEffects = cancel(state.call)
                    )

                    is ReceivingResult.ReceivingSucceeded -> transitionTo(
                        target = Receiving(updatedStatus),
                        withEffects = listOf(Reconnected, NewMessages(event.subscribeEnvelope.messages))
                    )
                    is ReceivingResult.ReceivingFailed -> if (retryPolicy.shouldRetry(state.extendedState.retryCounter)) {
                        transitionTo(state.copy(extendedState = state.extendedState.copy(retryCounter = state.extendedState.retryCounter + 1)))
                    } else {
                        transitionTo(target = ReconnectingFailed(updatedStatus), withEffects = Disconnected())
                    }
                    else -> noTransition()

                }
            is ReconnectingFailed -> when (event) {
                else -> noTransition() //TODO figure out transitions out of it
            }
        }
    }
