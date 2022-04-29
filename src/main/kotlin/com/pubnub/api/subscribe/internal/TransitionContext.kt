package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.StateMachineContext
import com.pubnub.api.state.TransitionContext

@StateMachineContext
internal class SubscribeTransitionContext(
    state: SubscribeState,
    event: SubscribeEvent
) : TransitionContext<SubscribeEffectInvocation, SubscribeExtendedState, SubscribeEvent, SubscribeState>(state, event) {
    override val updatedExtendedState: SubscribeExtendedState = state.extendedState + event
}

internal operator fun SubscribeExtendedState.plus(event: SubscribeEvent): SubscribeExtendedState {
    return when (event) {
        is HandshakingSuccess -> copy(cursor = event.cursor)
        is HandshakingFailure -> copy(attempt = 0)
        is ReceivingFailure -> copy(attempt = 0)
        is ReceivingSuccess -> copy(
            cursor = Cursor(
                timetoken = event.subscribeEnvelope.metadata.timetoken,
                region = event.subscribeEnvelope.metadata.region
            )
        )
        is SubscriptionChanged -> copy(
            channels = event.channels.toSet(),
            groups = event.groups.toSet()
        )
        is ReceiveReconnectingFailure -> copy(
            attempt = attempt + 1
        )
        is HandshakingReconnectingFailure -> copy(
            attempt = attempt + 1
        )
        is HandshakingReconnectingSuccess -> copy(
            cursor = event.cursor
        )
        is ReceiveReconnectingSuccess -> copy(
            cursor = Cursor(
                timetoken = event.subscribeEnvelope.metadata.timetoken,
                region = event.subscribeEnvelope.metadata.region
            )
        )
        is ReceiveReconnectingRetry -> copy(
            attempt = 0
        )
        else -> this
    }
}

internal fun SubscribeState.cancel(vararg effects: SubscribeEffectInvocation): List<SubscribeEffectInvocation> {
    return effects.flatMap { listOf(CancelEffectInvocation(it.id())) }
}
