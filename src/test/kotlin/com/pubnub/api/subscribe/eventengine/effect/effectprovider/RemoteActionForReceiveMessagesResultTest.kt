package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.models.server.SubscribeMetaData
import com.pubnub.api.subscribe.eventengine.effect.ReceiveMessagesResult
import com.pubnub.api.workers.SubscribeMessageProcessor
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoteActionForReceiveMessagesResultTest {
    private lateinit var objectUnderTest: RemoteActionForReceiveMessages
    private val subscribeRemoteAction: RemoteAction<SubscribeEnvelope> = mockk()
    private val messageProcessor: SubscribeMessageProcessor = mockk()

    @BeforeEach
    fun setUp() {
        objectUnderTest = RemoteActionForReceiveMessages(subscribeRemoteAction, messageProcessor)
    }

    @Test
    fun `should execute callback with proper values when async is called for RemoteActionForReceiveMessages`() {
        val subscribeRemoteActionSlot = slot<(result: SubscribeEnvelope?, status: PNStatus) -> Unit>()
        val expectedMessages: List<SubscribeMessage> = listOf(mockk())
        val expectedTimetoken = 11111306900762336
        val expectedRegion = "44"
        val expectedMetadata = SubscribeMetaData(expectedTimetoken, expectedRegion)
        val remoteActionResult: SubscribeEnvelope = mockk {
            every { messages } returns expectedMessages
            every { metadata } returns expectedMetadata
        }
        val remoteActionStatus: PNStatus = mockk()
        every { subscribeRemoteAction.async(capture(subscribeRemoteActionSlot)) } answers {
            subscribeRemoteActionSlot.captured(remoteActionResult, remoteActionStatus)
        }
        val sdkMessage: PNEvent = mockk()
        every { messageProcessor.processIncomingPayload(any()) } returns sdkMessage

        objectUnderTest.async { result, status ->
            assertEquals(sdkMessage, result?.messages?.get(0))
            assertEquals(expectedTimetoken, result?.subscriptionCursor?.timetoken)
            assertEquals(expectedRegion, result?.subscriptionCursor?.region)
            assertEquals(remoteActionStatus, status)
        }
    }

    @Test
    fun `should return result with proper values when sync`() {
        val messages: List<SubscribeMessage> = listOf(mockk())
        val timetoken = 11111306900762336
        val region = "44"
        val metadata = SubscribeMetaData(timetoken, region)
        val subscribeEnvelope = SubscribeEnvelope(messages, metadata)
        every { subscribeRemoteAction.sync() } returns subscribeEnvelope
        val sdkMessage: PNEvent = mockk()
        every { messageProcessor.processIncomingPayload(any()) } returns sdkMessage

        val receiveMessagesResult: ReceiveMessagesResult = objectUnderTest.sync()

        assertEquals(sdkMessage, receiveMessagesResult.messages[0])
        assertEquals(timetoken, receiveMessagesResult.subscriptionCursor.timetoken)
        assertEquals(region, receiveMessagesResult.subscriptionCursor.region)
    }

    @Test
    fun `should call silentCancel on remote action when silentCancel`() {
        every { subscribeRemoteAction.silentCancel() } returns Unit

        objectUnderTest.silentCancel()

        verify { subscribeRemoteAction.silentCancel() }
    }
}
