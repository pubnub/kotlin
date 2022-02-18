package com.pubnub.api.subscribe.internal

import com.pubnub.api.subscribe.*
import com.pubnub.api.subscribe.Commands.*
import com.pubnub.api.subscribe.HandshakeResult.*
import com.pubnub.api.subscribe.ReceivingResult.*

fun subscribeTransition(shouldRetry: (Int) -> Boolean): SubscribeTransition =
    defnTransition { state, event ->
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
