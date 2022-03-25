package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.Engine
import com.pubnub.api.state.EngineImplementation
import com.pubnub.api.state.transitionTo

typealias SubscribeEventEngine = Engine<SubscribeState, SubscribeEvent, SubscribeEffectInvocation>


internal fun subscribeEventEngine(shouldRetry: (Int) -> Boolean): Pair<SubscribeEventEngine, List<SubscribeEffectInvocation>> {
    val engine = EngineImplementation(
        initialState = Unsubscribed,
        transition = subscribeTransition(shouldRetry)
    )

    val effects = engine.transition(InitialEvent)
    return engine to effects
}

internal fun defineTransition(transitionFn: SubscribeTransitionContext.(SubscribeState, SubscribeEvent) -> Pair<SubscribeState, List<SubscribeEffectInvocation>>): SubscribeTransition {
    return { s, i ->
        val context = SubscribeTransitionContext(s, i)
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
