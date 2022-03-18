package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.internal.QueuedEventEngine
import com.pubnub.api.state.internal.EventEngine
import java.util.concurrent.LinkedBlockingQueue

typealias SubscribeEventEngine = EventEngine<SubscribeState, SubscribeEvent, SubscribeEffectInvocation>

typealias QueuedSubscribeEventEngine = QueuedEventEngine<SubscribeState, SubscribeEvent, SubscribeEffectInvocation>

internal fun subscribeEventEngine(shouldRetry: (Int) -> Boolean): Pair<SubscribeEventEngine, List<SubscribeEffectInvocation>> {
    return EventEngine.create(
        initialState = Unsubscribed,
        transition = subscribeTransition(shouldRetry),
        initialEvent = InitialEvent
    )
}

internal fun queuedSubscribeEventEngine(
    eventQueue: LinkedBlockingQueue<SubscribeEvent>,
    effectQueue: LinkedBlockingQueue<SubscribeEffectInvocation>,
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

internal fun defineTransition(transitionFn: TransitionContext.(SubscribeState, SubscribeEvent) -> Pair<SubscribeState, List<SubscribeEffectInvocation>>): SubscribeTransition {
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
