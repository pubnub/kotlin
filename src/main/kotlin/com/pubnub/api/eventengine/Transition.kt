package com.pubnub.api.eventengine

internal fun <EV : Event, EF : EffectInvocation, S : State<EF, EV, S>> transition(state: S, event: EV): Pair<S, List<EF>> = state.transition(event)
