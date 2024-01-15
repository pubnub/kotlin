package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.internal.eventengine.Cancel
import com.pubnub.internal.eventengine.EffectInvocation
import com.pubnub.internal.eventengine.EffectInvocationType
import com.pubnub.internal.eventengine.Managed
import com.pubnub.internal.eventengine.NonManaged

internal sealed class PresenceEffectInvocation(override val type: EffectInvocationType) : EffectInvocation {
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

    class DelayedHeartbeat(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val attempts: Int,
        val reason: PubNubException?
    ) : PresenceEffectInvocation(Managed) {
        override val id: String = DelayedHeartbeat::class.java.simpleName
    }

    object CancelDelayedHeartbeat :
        PresenceEffectInvocation(Cancel(idToCancel = DelayedHeartbeat::class.java.simpleName))
}
