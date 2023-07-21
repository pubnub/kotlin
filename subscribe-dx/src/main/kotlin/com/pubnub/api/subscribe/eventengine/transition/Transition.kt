package com.pubnub.api.subscribe.eventengine.transition

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.SubscribeState

fun transition(state: SubscribeState, event: Event): Pair<SubscribeState, List<SubscribeEffectInvocation>> = state.transition(event)
