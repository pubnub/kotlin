package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.StateMachine
import com.pubnub.api.state.Transition
import com.pubnub.api.subscribe.*
import com.pubnub.api.subscribe.Commands.*
import com.pubnub.api.subscribe.HandshakeResult.*
import com.pubnub.api.subscribe.ReceivingResult.*

data class TransitionsDescriptionContext(
    val state: SubscribeState,
    val event: SubscribeEvent
) {
    val updatedStatus: SubscriptionStatus = state.status + event
}

operator fun SubscriptionStatus.plus(event: SubscribeEvent): SubscriptionStatus {
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

//Clojure4life ;)
fun defnTransition(transitionFn: TransitionsDescriptionContext.(SubscribeState, SubscribeEvent) -> Pair<SubscribeState, Collection<AbstractSubscribeEffect>>): SubscribeTransition {
    return { s, i ->
        val context = TransitionsDescriptionContext(s, i)
        if (i is InitialEvent) {
            val (_, newEffects) = context.transitionTo(s)
            s to s.onEntry() + newEffects
        }

        val (newState, newEffects) = context.transitionFn(s, i)
        if (newEffects.any { it is NewState }) {
            newState to (s.onExit() + newEffects + newState.onEntry())
        } else {
            newState to newEffects
        }
    }
}


fun TransitionsDescriptionContext.noTransition(): Pair<SubscribeState, Collection<AbstractSubscribeEffect>> {
    return state to listOf()
}

fun TransitionsDescriptionContext.transitionTo(
    target: SubscribeState
): Pair<SubscribeState, Collection<AbstractSubscribeEffect>> {
    return target to listOf(NewState(target::class.simpleName!!, target.status))
}

fun TransitionsDescriptionContext.transitionTo(
    target: SubscribeState,
    onExit: AbstractSubscribeEffect
): Pair<SubscribeState, Collection<AbstractSubscribeEffect>> {
    return target to listOf(NewState(target::class.simpleName!!, target.status), onExit)
}

fun TransitionsDescriptionContext.transitionTo(
    target: SubscribeState,
    onExit: Collection<AbstractSubscribeEffect> = listOf()
): Pair<SubscribeState, Collection<AbstractSubscribeEffect>> {
    return target to listOf(NewState(target::class.simpleName!!, target.status)) + onExit
}

fun TransitionsDescriptionContext.cancel(vararg effects: AbstractSubscribeEffect): Collection<CancelEffect> {
    return effects.flatMap { (it.child?.let { child -> cancel(child) } ?: listOf()) + listOf(CancelEffect(it.id)) }
}

typealias SubscribeTransition = Transition<SubscribeState, SubscribeEvent, AbstractSubscribeEffect>

typealias SubscribeMachine = StateMachine<SubscribeEvent, AbstractSubscribeEffect>

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
