package com.pubnub.api.eventengine

import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

interface EventEnginesConf {
    val subscribeEventSink: Sink<SubscribeEvent>
    val subscribeEventSource: Source<SubscribeEvent>
    val subscribeEffectSink: Sink<SubscribeEffectInvocation>
    val subscribeEffectSource: Source<SubscribeEffectInvocation>

    val presenceEventSink: Sink<PresenceEvent>
    val presenceEventSource: Source<PresenceEvent>
    val presenceEffectSink: Sink<PresenceEffectInvocation>
    val presenceEffectSource: Source<PresenceEffectInvocation>
}
