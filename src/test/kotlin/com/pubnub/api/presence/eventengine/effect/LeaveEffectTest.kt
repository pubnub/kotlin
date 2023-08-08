package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.subscribe.eventengine.effect.failingRemoteAction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LeaveEffectTest {
    private val reason = PubNubException("Unknown error")

    @Test
    fun `should throw exception when LeaveEffect failed`() {
        // given
        val leaveEffect = LeaveEffect(failingRemoteAction(reason))

        // when
        try {
            leaveEffect.runEffect()
        } catch (exception: Exception) {
            assertEquals("LeaveEffect failed", exception.message)
        }
    }
}
