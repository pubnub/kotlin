package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class LeaveEffectTest {

    private val leaveRemoteAction: RemoteAction<Boolean> = mockk()

    @Test
    fun `when suppressLeaveEvents is true should not call leaveRemoteAction`() {
        // given
        val suppressLeaveEvents = true
        val leaveEffect = LeaveEffect(leaveRemoteAction, suppressLeaveEvents)

        // when
        leaveEffect.runEffect()

        // then
        verify(exactly = 0) { leaveRemoteAction.async { _, _ -> } }
    }

    @Test
    fun `when suppressLeaveEvents is false should call leaveRemoteAction`() {
        // given
        val suppressLeaveEvents = false
        val leaveEffect = LeaveEffect(leaveRemoteAction, suppressLeaveEvents)
        every { leaveRemoteAction.async(any()) } just Runs

        // when
        leaveEffect.runEffect()

        // then
        verify { leaveRemoteAction.async(any()) }
    }
}
