package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.eventengine.EventEngine
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.state.SubscribeState

typealias SubscribeEventEngine = EventEngine<SubscribeEffectInvocation, SubscribeEvent, SubscribeState>

fun SubscribeEventEngine(
    effectSink: Sink<SubscribeEffectInvocation>,
    eventSource: Source<SubscribeEvent>,
    currentState: SubscribeState = SubscribeState.Unsubscribed
): SubscribeEventEngine = EventEngine(
    effectSink, eventSource, currentState
)
