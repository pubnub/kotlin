package com.pubnub.api.managers

import com.pubnub.api.eventengine.EventEngineManager
import com.pubnub.api.presence.eventengine.PresenceEventEngine
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState

typealias PresenceEventEngineManager = EventEngineManager<PresenceEffectInvocation, PresenceEvent, PresenceState, PresenceEventEngine>
