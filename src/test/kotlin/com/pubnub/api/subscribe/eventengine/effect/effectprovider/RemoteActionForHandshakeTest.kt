package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoteActionForHandshakeTest {
    private lateinit var objectUnderTest: RemoteActionForHandshake
    private val subscribeRemoteAction: RemoteAction<SubscribeEnvelope> = mockk()

    @BeforeEach
    fun setUp() {
        objectUnderTest = RemoteActionForHandshake(subscribeRemoteAction)
    }

    @Test
    fun `should execute callback with proper values when async`() {
        val remoteActionLambda: CapturingSlot<(SubscribeEnvelope?, PNStatus) -> Unit> = slot()
        val remoteActionResult: SubscribeEnvelope = mockk()
        val remoteActionStatus: PNStatus = mockk()
        val timetoken = System.currentTimeMillis()
        val region = "99"
        val subscribeMetaData = SubscribeMetaData(timetoken = timetoken, region = region)

        every { remoteActionResult.metadata } returns subscribeMetaData

        every { subscribeRemoteAction.async(capture(remoteActionLambda)) } answers {
            remoteActionLambda.captured(remoteActionResult, remoteActionStatus)
        }

        objectUnderTest.async { result, status ->

            assertEquals(timetoken, result?.timetoken)
            assertEquals(region, result?.region)
            assertEquals(remoteActionStatus, status)
        }

        verify { subscribeRemoteAction.async(any()) }
    }

    @Test
    fun `should return subscription cursor with proper values when sync`() {
        val timetoken = System.currentTimeMillis()
        val region = "99"
        val subscribeMetaData = SubscribeMetaData(timetoken = timetoken, region = region)
        val subscribeEnvelope = SubscribeEnvelope(messages = listOf(), metadata = subscribeMetaData)
        every { subscribeRemoteAction.sync() } returns subscribeEnvelope

        val subscriptionCursor = objectUnderTest.sync()

        assertEquals(timetoken, subscriptionCursor?.timetoken)
        assertEquals(region, subscriptionCursor?.region)
    }

    @Test
    fun `should call silentCancel on remote action when silentCancel`() {
        every { subscribeRemoteAction.silentCancel() } returns Unit

        objectUnderTest.silentCancel()

        verify(exactly = 1) { subscribeRemoteAction.silentCancel() }
    }
}
