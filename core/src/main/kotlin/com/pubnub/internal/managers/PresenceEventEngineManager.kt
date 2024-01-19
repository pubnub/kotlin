package com.pubnub.internal.managers

import com.pubnub.internal.eventengine.EventEngineManager
import com.pubnub.internal.presence.eventengine.PresenceEventEngine
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.presence.eventengine.state.PresenceState

internal typealias PresenceEventEngineManager = EventEngineManager<PresenceEffectInvocation, PresenceEvent, PresenceState, PresenceEventEngine>
