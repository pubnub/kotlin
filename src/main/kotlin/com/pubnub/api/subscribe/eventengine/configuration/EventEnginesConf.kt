package com.pubnub.api.subscribe.eventengine.configuration

import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueEventEngineConf
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent

class EventEnginesConf(
    val subscribe: EventEngineConf<SubscribeEffectInvocation, SubscribeEvent> = QueueEventEngineConf(),
    val presence: EventEngineConf<PresenceEffectInvocation, PresenceEvent> = QueueEventEngineConf()
)
