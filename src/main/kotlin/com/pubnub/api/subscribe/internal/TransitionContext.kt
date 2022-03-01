package com.pubnub.api.subscribe.internal

import com.pubnub.api.subscribe.internal.Commands.*
import com.pubnub.api.subscribe.internal.HandshakeResult.HandshakeSucceeded
import com.pubnub.api.subscribe.internal.ReceivingResult.ReceivingSucceeded

/**
 * A class used to improve TODO improve what exactly? ;)
 */

@DslMarker
annotation class StateMachineContext

@StateMachineContext
internal data class TransitionContext(
    val state: SubscribeState,
    val event: SubscribeEvent
) {
    val updatedStatus: SubscriptionStatus = state.status + event
}

internal operator fun SubscriptionStatus.plus(event: SubscribeEvent): SubscriptionStatus {
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
        UnsubscribeAllIssued -> SubscriptionStatus()
        is UnsubscribeIssued -> copy(
            channels = channels - event.channels.toSet(),
            groups = groups - event.groups.toSet(),
        )
        else -> this
    }
}

internal fun TransitionContext.noTransition(): Pair<SubscribeState, Collection<SubscribeEffect>> {
    return state to listOf()
}

internal fun TransitionContext.transitionTo(
    target: SubscribeState
): Pair<SubscribeState, Collection<SubscribeEffect>> {
    return target to listOf(NewState(target::class.simpleName!!, target.status))
}

internal fun TransitionContext.transitionTo(
    target: SubscribeState,
    onExit: SubscribeEffect
): Pair<SubscribeState, Collection<SubscribeEffect>> {
    return target to listOf(NewState(target::class.simpleName!!, target.status), onExit)
}

internal fun TransitionContext.transitionTo(
    target: SubscribeState,
    onExit: Collection<SubscribeEffect> = listOf()
): Pair<SubscribeState, Collection<SubscribeEffect>> {
    return target to listOf(NewState(target::class.simpleName!!, target.status)) + onExit
}

internal fun TransitionContext.cancel(vararg effects: SubscribeEffect): Collection<CancelEffect> {
    return effects.flatMap { (it.child?.let { child -> cancel(child) } ?: listOf()) + listOf(CancelEffect(it.id)) }
}

