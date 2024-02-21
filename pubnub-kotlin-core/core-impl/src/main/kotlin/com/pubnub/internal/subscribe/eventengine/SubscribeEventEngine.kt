package com.pubnub.internal.subscribe.eventengine

import com.pubnub.internal.eventengine.EventEngine
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.eventengine.Source
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.state.SubscribeState

internal typealias SubscribeEventEngine = EventEngine<SubscribeEffectInvocation, SubscribeEvent, SubscribeState>

internal fun SubscribeEventEngine(
    effectSink: Sink<SubscribeEffectInvocation>,
    eventSource: Source<SubscribeEvent>,
    currentState: SubscribeState = SubscribeState.Unsubscribed
): SubscribeEventEngine = EventEngine(
    effectSink, eventSource, currentState
)
