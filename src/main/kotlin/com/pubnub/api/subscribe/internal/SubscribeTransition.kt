package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.Transition
import com.pubnub.api.state.noTransition
import com.pubnub.api.state.transitionTo

typealias SubscribeTransition = Transition<SubscribeState, SubscribeEvent, SubscribeEffectInvocation>

internal fun subscribeTransition(): SubscribeTransition =
    defineTransition { state, event ->
        when (state) {
            is Handshaking -> when (event) {
                is SubscriptionChanged -> transitionTo(
                    target = Handshaking(updatedExtendedState)
                )
                is Disconnect -> transitionTo(
                    target = Preparing(updatedExtendedState)
                )
                is HandshakingFailure -> transitionTo(
                    target = HandshakeReconnecting(updatedExtendedState)
                )
                is HandshakingSuccess -> transitionTo(
                    target = Receiving(updatedExtendedState),
                    withEffects = Connected
                )
                else -> noTransition()
            }
            is Receiving -> {
                when (event) {
                    is SubscriptionChanged -> transitionTo(
                        target = Receiving(updatedExtendedState)
                    )
                    is Disconnect -> transitionTo(
                        target = Preparing(updatedExtendedState)
                    )
                    is ReceivingFailure -> transitionTo(
                        target = ReceiveReconnecting(updatedExtendedState)
                    )
                    is ReceivingSuccess -> transitionTo(
                        target = Receiving(updatedExtendedState),
                        withEffects = EmitEvents(event.subscribeEnvelope.messages)
                    )
                    else -> noTransition()
                }
            }
            Unsubscribed -> when (event) {
                is SubscriptionChanged -> transitionTo(
                    target = Handshaking(updatedExtendedState)
                )
                else -> noTransition()
            }
            is HandshakingFailed -> when (event) {
                is SubscriptionChanged -> transitionTo(
                    target = Handshaking(updatedExtendedState)
                )
                is ReceiveReconnectingRetry -> transitionTo(
                    target = HandshakeReconnecting(updatedExtendedState)
                )
                else -> noTransition()
            }
            is ReceiveReconnecting ->
                when (event) {
                    is SubscriptionChanged -> transitionTo(
                        target = ReceiveReconnecting(updatedExtendedState)
                    )
                    is ReceiveReconnectingSuccess -> transitionTo(
                        target = Receiving(updatedExtendedState),
                        withEffects = listOf(EmitEvents(event.subscribeEnvelope.messages), Reconnected)
                    )
                    is ReceiveReconnectingFailure -> transitionTo(
                        target = ReceiveReconnecting(updatedExtendedState)
                    )
                    is ReceiveReconnectingGiveUp -> transitionTo(
                        target = ReconnectingFailed(updatedExtendedState)
                    )
                    else -> noTransition()
                }
            is ReconnectingFailed -> when (event) {
                is ReceiveReconnectingRetry -> transitionTo(
                    target = ReceiveReconnecting(updatedExtendedState)
                )
                else -> noTransition()
            }
            is HandshakeReconnecting -> when (event) {
                is SubscriptionChanged -> transitionTo(
                    target = HandshakeReconnecting(updatedExtendedState)
                )
                is HandshakingReconnectingSuccess -> transitionTo(
                    target = Receiving(updatedExtendedState),
                    withEffects = Connected
                )
                is HandshakingReconnectingFailure -> transitionTo(
                    target = HandshakeReconnecting(updatedExtendedState)
                )
                is HandshakingReconnectingGiveUp -> transitionTo(
                    target = HandshakingFailed(updatedExtendedState)
                )
                is HandshakingReconnectingRetry -> noTransition()
                else -> noTransition()
            }
            is Paused -> TODO()
            is Preparing -> TODO()
        }
    }
