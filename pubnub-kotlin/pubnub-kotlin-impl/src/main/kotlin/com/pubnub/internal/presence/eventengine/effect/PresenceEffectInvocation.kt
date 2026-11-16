package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.internal.eventengine.Cancel
import com.pubnub.internal.eventengine.EffectInvocation
import com.pubnub.internal.eventengine.EffectInvocationType
import com.pubnub.internal.eventengine.Managed
import com.pubnub.internal.eventengine.NonManaged

internal sealed class PresenceEffectInvocation(override val type: EffectInvocationType) : EffectInvocation {
    override val id: String = "any value for NonManged and Cancel effect"

    /**
     * Managed with a fixed id, so dispatching a new heartbeat cancels the previous one (and any retry it scheduled),
     * and [CancelHeartbeat] can cancel it before a `Leave` goes out.
     */
    data class Heartbeat(
        val channels: Set<String>,
        val channelGroups: Set<String>,
    ) : PresenceEffectInvocation(Managed) {
        override val id: String = Heartbeat::class.java.simpleName
    }

    object CancelHeartbeat : PresenceEffectInvocation(Cancel(idToCancel = Heartbeat::class.java.simpleName))

    data class Leave(
        val channels: Set<String>,
        val channelGroups: Set<String>,
    ) : PresenceEffectInvocation(NonManaged)

    class Wait : PresenceEffectInvocation(Managed) {
        override val id: String = Wait::class.java.simpleName
    }

    object CancelWait : PresenceEffectInvocation(Cancel(idToCancel = Wait::class.java.simpleName))
}
