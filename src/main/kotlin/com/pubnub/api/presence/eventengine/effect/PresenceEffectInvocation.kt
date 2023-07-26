package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.eventengine.Cancel
import com.pubnub.api.eventengine.EffectInvocation
import com.pubnub.api.eventengine.EffectInvocationType
import com.pubnub.api.eventengine.Managed
import com.pubnub.api.eventengine.NonManaged

sealed class PresenceEffectInvocation(override val type: EffectInvocationType) : EffectInvocation {
    override val id: String = "any value for NonManged and Cancel effect"

    data class Heartbeat(
        val channels: Set<String>,
        val channelGroups: Set<String>,
    ) : PresenceEffectInvocation(NonManaged)

    data class Leave(
        val channels: Set<String>,
        val channelGroups: Set<String>,
    ) : PresenceEffectInvocation(NonManaged)

    class Wait : PresenceEffectInvocation(Managed) {
        override val id: String = Wait::class.java.simpleName
    }

    object CancelWait : PresenceEffectInvocation(Cancel(idToCancel = Wait::class.java.simpleName))

    class DelayedHeartbeat : PresenceEffectInvocation(Managed) {
        override val id: String = DelayedHeartbeat::class.java.simpleName
    }

    object CancelDelayedHeartbeat :
        PresenceEffectInvocation(Cancel(idToCancel = DelayedHeartbeat::class.java.simpleName))
}
