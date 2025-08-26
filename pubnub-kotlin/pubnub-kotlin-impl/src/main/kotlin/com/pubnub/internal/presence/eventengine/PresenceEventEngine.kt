package com.pubnub.internal.presence.eventengine

import com.pubnub.internal.eventengine.EventEngine
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.eventengine.Source
import com.pubnub.internal.logging.LogConfig
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.presence.eventengine.state.PresenceState

internal typealias PresenceEventEngine = EventEngine<PresenceEffectInvocation, PresenceEvent, PresenceState>

internal fun PresenceEventEngine(
    effectSink: Sink<PresenceEffectInvocation>,
    eventSource: Source<PresenceEvent>,
    logConfig: LogConfig,
    currentState: PresenceState = PresenceState.HeartbeatInactive,
): PresenceEventEngine = EventEngine(effectSink, eventSource, currentState, logConfig)
