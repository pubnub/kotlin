package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.Machine
import com.pubnub.api.state.Transitions
import com.pubnub.api.subscribe.*

data class TransitionsDescriptionContext(
    val state: SubscribeState,
    val event: SubscribeEvent
) {
    val updatedStatus: SubscriptionStatus = state.status + event

    operator fun SubscriptionStatus.plus(event: SubscribeEvent): SubscriptionStatus {
        return when (event) {
            is HandshakeResult.HandshakeSucceeded -> copy(cursor = event.cursor)
            is ReceivingResult.ReceivingSucceeded -> copy(
                cursor = Cursor(
                    timetoken = event.subscribeEnvelope.metadata.timetoken,
                    region = event.subscribeEnvelope.metadata.region
                )
            )
            is Commands.SubscribeIssued -> copy(
                channels = channels + event.channels.toSet(),
                groups = groups + event.groups.toSet(),
                cursor = event.cursor ?: cursor
            )
            Commands.UnsubscribeAllIssued -> SubscriptionStatus()
            is Commands.UnsubscribeIssued -> copy(
                channels = channels - event.channels.toSet(),
                groups = groups - event.groups.toSet(),
            )
            else -> this
        }
    }

}

fun transitions(hugeSwitch: TransitionsDescriptionContext.(SubscribeState, SubscribeEvent) -> Pair<SubscribeState, Collection<AbstractSubscribeEffect>>): SubscribeTransitions {
    return { s, i ->
        val (newState, newEffects) = TransitionsDescriptionContext(s, i).hugeSwitch(s, i)
        if (s == newState) {
            newState to newEffects
        } else {
            newState to (s.onExit() + NewState(
                name = newState::class.simpleName!!,
                status = newState.status
            ) + newState.onEntry() + newEffects)
        }
    }
}


fun TransitionsDescriptionContext.noTransition(): Pair<SubscribeState, Collection<AbstractSubscribeEffect>> {
    return state to listOf()
}

fun TransitionsDescriptionContext.transitionTo(
    target: SubscribeState
): Pair<SubscribeState, Collection<AbstractSubscribeEffect>> {
    return target to listOf()
}

fun TransitionsDescriptionContext.transitionTo(
    target: SubscribeState,
    onExit: AbstractSubscribeEffect? = null
): Pair<SubscribeState, Collection<AbstractSubscribeEffect>> {
    return target to (onExit?.let { listOf(it) } ?: listOf())
}

fun TransitionsDescriptionContext.transitionTo(
    target: SubscribeState,
    onExit: Collection<AbstractSubscribeEffect> = listOf()
): Pair<SubscribeState, Collection<AbstractSubscribeEffect>> {
    return target to onExit
}

fun TransitionsDescriptionContext.cancel(vararg effects: AbstractSubscribeEffect): Collection<CancelEffect> {
    return effects.flatMap { (it.child?.let { child -> cancel(child) } ?: listOf()) + listOf(CancelEffect(it.id))  }
}

typealias SubscribeTransitions = Transitions<SubscribeState, SubscribeEvent, AbstractSubscribeEffect>

typealias SubscribeMachine = Machine<SubscribeEvent, AbstractSubscribeEffect>

/**
 * Use from single thread only
 */
fun subscribeMachine(
    shouldRetry: (Int) -> Boolean,
    transitions: SubscribeTransitions = subscribeTransitions(shouldRetry),
    initState: SubscribeState = Unsubscribed,
): SubscribeMachine {
    var state = initState
    return { event ->
        val (ns, effects) = transitions(state, event)
        state = ns
        effects
    }
}



