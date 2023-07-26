package com.pubnub.api.eventengine

internal fun <EV : Event, EF : EffectInvocation, S : State<EF, EV, S>> transition(state: S, event: EV): Pair<S, Set<EF>> = state.transition(event)
