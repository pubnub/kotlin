package com.pubnub.internal.presence.eventengine.effect

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
    fun `when runEffect is executed should call leaveRemoteAction`() {
        // given
        val leaveEffect = LeaveEffect(leaveRemoteAction)
        every { leaveRemoteAction.async(any()) } just Runs

        // when
        leaveEffect.runEffect()

        // then
        verify { leaveRemoteAction.async(any()) }
    }
}
