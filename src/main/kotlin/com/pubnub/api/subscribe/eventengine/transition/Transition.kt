package com.pubnub.api.subscribe.eventengine.transition

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.state.SubscribeState

internal fun transition(state: SubscribeState, subscribeEvent: SubscribeEvent): Pair<SubscribeState, List<SubscribeEffectInvocation>> = state.transition(subscribeEvent)
