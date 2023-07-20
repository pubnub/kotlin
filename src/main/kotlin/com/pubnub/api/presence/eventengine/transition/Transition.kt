package com.pubnub.api.presence.eventengine.transition

import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState

internal fun transition(state: PresenceState, presenceEvent: PresenceEvent): Pair<PresenceState, List<PresenceEffectInvocation>> = state.transition(presenceEvent)
