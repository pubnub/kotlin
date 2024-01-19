package com.pubnub.internal.managers

import com.pubnub.internal.eventengine.EventEngineManager
import com.pubnub.internal.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.state.SubscribeState

internal typealias SubscribeEventEngineManager = EventEngineManager<SubscribeEffectInvocation, SubscribeEvent, SubscribeState, SubscribeEventEngine>
