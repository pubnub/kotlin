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
                    target = HandshakingReconnecting(updatedExtendedState)
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
                        target = Reconnecting(updatedExtendedState)
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
                is ReconnectingRetry -> transitionTo(
                    target = HandshakingReconnecting(updatedExtendedState)
                )
                else -> noTransition()
            }
            is Reconnecting ->
                when (event) {
                    is SubscriptionChanged -> transitionTo(
                        target = Reconnecting(updatedExtendedState)
                    )
                    is ReconnectingSuccess -> transitionTo(
                        target = Receiving(updatedExtendedState),
                        withEffects = listOf(EmitEvents(event.subscribeEnvelope.messages), Reconnected)
                    )
                    is ReconnectingFailure -> transitionTo(
                        target = Reconnecting(updatedExtendedState)
                    )
                    is ReconnectingGiveUp -> transitionTo(
                        target = ReconnectingFailed(updatedExtendedState)
                    )
                    else -> noTransition()
                }
            is ReconnectingFailed -> when (event) {
                is ReconnectingRetry -> transitionTo(
                    target = Reconnecting(updatedExtendedState)
                )
                else -> noTransition()
            }
            is HandshakingReconnecting ->                 when (event) {
                is SubscriptionChanged -> transitionTo(
                    target = HandshakingReconnecting(updatedExtendedState)
                )
                is HandshakingReconnectingSuccess -> transitionTo(
                    target = Receiving(updatedExtendedState),
                    withEffects = Connected
                )
                is HandshakingReconnectingFailure -> transitionTo(
                    target = HandshakingReconnecting(updatedExtendedState)
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
