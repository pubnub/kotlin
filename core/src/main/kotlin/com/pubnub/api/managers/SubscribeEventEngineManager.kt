package com.pubnub.api.managers

import com.pubnub.api.eventengine.EventEngineManager
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.state.SubscribeState

internal typealias SubscribeEventEngineManager = EventEngineManager<SubscribeEffectInvocation, SubscribeEvent, SubscribeState, SubscribeEventEngine>
