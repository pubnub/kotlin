package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.QueuedEventEngine
import com.pubnub.api.state.EventEngine
import com.pubnub.api.state.Transition
import com.pubnub.api.subscribe.internal.Commands.*
import com.pubnub.api.subscribe.internal.HandshakeResult.HandshakeFailed
import com.pubnub.api.subscribe.internal.HandshakeResult.HandshakeSucceeded
import com.pubnub.api.subscribe.internal.ReceivingResult.ReceivingFailed
import com.pubnub.api.subscribe.internal.ReceivingResult.ReceivingSucceeded
import java.util.concurrent.LinkedBlockingQueue

typealias SubscribeEventEngine = EventEngine<SubscribeState, SubscribeEvent, SubscribeEffect>

typealias QueuedSubscribeEventEngine = QueuedEventEngine<SubscribeState, SubscribeEvent, SubscribeEffect>

internal fun subscribeEventEngine(shouldRetry: (Int) -> Boolean): Pair<SubscribeEventEngine, Collection<SubscribeEffect>> {
    return EventEngine.create(
        initialState = Unsubscribed,
        transition = subscribeTransition(shouldRetry),
        initialEvent = InitialEvent
    )
}

internal fun queuedSubscribeEventEngine(
    eventQueue: LinkedBlockingQueue<SubscribeEvent>,
    effectQueue: LinkedBlockingQueue<SubscribeEffect>,
    shouldRetry: (Int) -> Boolean
): QueuedSubscribeEventEngine {
    val (stateMachine, initialEffects) = subscribeEventEngine(shouldRetry)
    return QueuedSubscribeEventEngine.create(
        eventEngine = stateMachine,
        initialEffects = initialEffects,
        eventQueue = eventQueue,
        effectQueue = effectQueue
    )
}

internal fun defineTransition(transitionFn: TransitionContext.(SubscribeState, SubscribeEvent) -> Pair<SubscribeState, Collection<SubscribeEffect>>): SubscribeTransition {
    return { s, i ->
        val context = TransitionContext(s, i)
        if (i is InitialEvent) {
            val (_, newEffects) = context.transitionTo(s)
            s to (s.onEntry() + newEffects)
        } else {
            val (newState, newEffects) = context.transitionFn(s, i)
            if (newEffects.any { it is NewState }) {
                newState to (s.onExit() + newEffects + newState.onEntry())
            } else {
                newState to newEffects
            }
        }
    }
}
