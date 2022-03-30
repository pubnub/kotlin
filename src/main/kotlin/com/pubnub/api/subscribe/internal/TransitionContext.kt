package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.*
import com.pubnub.api.state.TransitionContext
import com.pubnub.api.subscribe.internal.Commands.*
import com.pubnub.api.subscribe.internal.HandshakeResult.HandshakeSucceeded
import com.pubnub.api.subscribe.internal.ReceivingResult.ReceivingSucceeded

/**
 * A class used to improve TODO improve what exactly? ;)
 */


@StateMachineContext
internal class SubscribeTransitionContext(
    state: SubscribeState,
    event: SubscribeEvent
) : TransitionContext<SubscribeEffectInvocation, SubscribeExtendedState, SubscribeEvent, SubscribeState>(state, event) {
    override val updatedStatus: SubscribeExtendedState = state.extendedState + event
}

internal operator fun SubscribeExtendedState.plus(event: SubscribeEvent): SubscribeExtendedState {
    return when (event) {
        is HandshakeSucceeded -> copy(cursor = event.cursor)
        is ReceivingSucceeded -> copy(
            cursor = Cursor(
                timetoken = event.subscribeEnvelope.metadata.timetoken,
                region = event.subscribeEnvelope.metadata.region
            )
        )
        is SubscribeIssued -> copy(
            channels = channels + event.channels.toSet(),
            groups = groups + event.groups.toSet(),
            cursor = event.cursor ?: cursor
        )
        UnsubscribeAllIssued -> SubscribeExtendedState()
        is UnsubscribeIssued -> copy(
            channels = channels - event.channels.toSet(),
            groups = groups - event.groups.toSet(),
        )
        else -> this
    }
}

internal fun SubscribeTransitionContext.cancel(vararg effects: SubscribeEffectInvocation): List<SubscribeEffectInvocation> {
    return effects.flatMap { listOf(CancelEffectInvocation(it.id())) }
}
