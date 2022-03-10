package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.Transition

typealias SubscribeTransition = Transition<SubscribeState, SubscribeEvent, SubscribeEffect>

internal fun subscribeTransition(shouldRetry: (Int) -> Boolean): SubscribeTransition =
    defineTransition { state, event ->
        when (state) {
            is Handshaking -> when (event) {
                is Commands.SubscribeIssued -> transitionTo(
                    target = Handshaking(updatedStatus),
                    onExit = cancel(state.call)
                )
                Commands.UnsubscribeAllIssued -> transitionTo(
                    target = Unsubscribed,
                    onExit = cancel(state.call)
                )
                is Commands.UnsubscribeIssued -> transitionTo(
                    target = Handshaking(updatedStatus),
                    onExit = cancel(state.call)
                )
                is HandshakeResult.HandshakeSucceeded -> transitionTo(Receiving(updatedStatus))
                is HandshakeResult.HandshakeFailed -> if (shouldRetry(state.retryCount)) {
                    transitionTo(state.copy(retryCount = state.retryCount + 1))
                } else {
                    transitionTo(HandshakingFailed(updatedStatus))
                }
                else -> noTransition()
            }
            is Receiving -> {
                when (event) {
                    is Commands.SubscribeIssued -> transitionTo(
                        target = Receiving(updatedStatus),
                        onExit = cancel(state.call)
                    )
                    Commands.UnsubscribeAllIssued -> transitionTo(
                        target = Unsubscribed,
                        onExit = cancel(state.call)
                    )
                    is Commands.UnsubscribeIssued -> transitionTo(
                        target = Receiving(updatedStatus),
                        onExit = cancel(state.call)
                    )

                    is ReceivingResult.ReceivingFailed -> transitionTo(Reconnecting(updatedStatus))
                    is ReceivingResult.ReceivingSucceeded -> transitionTo(
                        target = Receiving(updatedStatus),
                        onExit = NewMessages(event.subscribeEnvelope.messages)
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
                        onExit = cancel(state.call)
                    )
                    Commands.UnsubscribeAllIssued -> transitionTo(
                        target = Unsubscribed,
                        onExit = cancel(state.call)
                    )
                    is Commands.UnsubscribeIssued -> transitionTo(
                        target = Reconnecting(updatedStatus),
                        onExit = cancel(state.call)
                    )

                    is ReceivingResult.ReceivingSucceeded -> transitionTo(
                        target = Receiving(updatedStatus),
                        onExit = NewMessages(event.subscribeEnvelope.messages)
                    )
                    is ReceivingResult.ReceivingFailed -> if (shouldRetry(state.retryCount)) {
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
