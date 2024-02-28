package com.pubnub.internal.subscribe.eventengine.configuration

import com.pubnub.internal.eventengine.EventEngineConf
import com.pubnub.internal.eventengine.QueueEventEngineConf
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent

internal class EventEnginesConf(
    val subscribe: EventEngineConf<SubscribeEffectInvocation, SubscribeEvent> = QueueEventEngineConf(),
    val presence: EventEngineConf<PresenceEffectInvocation, PresenceEvent> = QueueEventEngineConf(),
)
