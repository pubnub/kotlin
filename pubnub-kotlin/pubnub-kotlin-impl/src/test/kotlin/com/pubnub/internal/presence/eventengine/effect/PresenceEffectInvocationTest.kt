package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.internal.eventengine.Cancel
import com.pubnub.internal.eventengine.Managed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class PresenceEffectInvocationTest {
    @Test
    fun `Heartbeat is managed with the same id for any channels, so a new heartbeat cancels the previous one`() {
        val first = PresenceEffectInvocation.Heartbeat(setOf("ch1"), emptySet())
        val second = PresenceEffectInvocation.Heartbeat(setOf("ch2"), setOf("cg1"))

        assertEquals(Managed, first.type)
        assertEquals(first.id, second.id)
    }

    @Test
    fun `CancelHeartbeat cancels the Heartbeat id`() {
        val heartbeat = PresenceEffectInvocation.Heartbeat(setOf("ch1"), emptySet())

        assertEquals(Cancel(idToCancel = heartbeat.id), PresenceEffectInvocation.CancelHeartbeat.type)
    }

    @Test
    fun `CancelHeartbeat and CancelWait cancel different effects`() {
        assertNotEquals(PresenceEffectInvocation.CancelWait.type, PresenceEffectInvocation.CancelHeartbeat.type)
    }
}
