package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.eventengine.Cancel
import com.pubnub.api.eventengine.EffectInvocation
import com.pubnub.api.eventengine.EffectInvocationType
import com.pubnub.api.eventengine.Managed

sealed class PresenceEffectInvocation(override val type: EffectInvocationType) : EffectInvocation {
    override val id: String = "any" // todo does value of this matter?

    data class Heartbeat(
        val channels: Set<String>,
        val channelGroups: Set<String>,
    ) : PresenceEffectInvocation(Managed)

    data class Leave(
        val channels: Set<String>,
        val channelGroups: Set<String>,
    ) : PresenceEffectInvocation(Managed)

    class ScheduleNextHeartbeat : PresenceEffectInvocation(Managed) {
        override val id: String = ScheduleNextHeartbeat::class.java.simpleName
    }

    object CancelScheduleNextHeartbeat :
        PresenceEffectInvocation(Cancel(idToCancel = ScheduleNextHeartbeat::class.java.simpleName))

    class DelayedHeartbeat : PresenceEffectInvocation(Managed) {
        override val id: String = DelayedHeartbeat::class.java.simpleName
    }

    object CancelDelayedHeartbeat :
        PresenceEffectInvocation(Cancel(idToCancel = DelayedHeartbeat::class.java.simpleName))
}
