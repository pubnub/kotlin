package com.pubnub.api.subscribe.eventengine.transition

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State

internal fun transition(state: State, event: Event): Pair<State, List<SubscribeEffectInvocation>> = state.transition(event)
