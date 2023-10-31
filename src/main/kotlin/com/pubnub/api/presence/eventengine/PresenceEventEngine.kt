package com.pubnub.api.presence.eventengine

import com.pubnub.api.eventengine.EventEngine
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState

internal typealias PresenceEventEngine = EventEngine<PresenceEffectInvocation, PresenceEvent, PresenceState>

internal fun PresenceEventEngine(
    effectSink: Sink<PresenceEffectInvocation>,
    eventSource: Source<PresenceEvent>,
    currentState: PresenceState = PresenceState.HeartbeatInactive,
): PresenceEventEngine = EventEngine(effectSink, eventSource, currentState)
