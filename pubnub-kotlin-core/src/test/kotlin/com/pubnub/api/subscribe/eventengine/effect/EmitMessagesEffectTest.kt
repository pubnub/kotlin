package com.pubnub.api.subscribe.eventengine.effect

import com.google.gson.JsonPrimitive
import com.pubnub.api.models.consumer.files.PNDownloadableFile
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EmitMessagesEffectTest {
    private val messagesConsumer: MessagesConsumer = mockk()
    private val message = "Message1"
    private val messageActionType = "reaction"
    private val objectEventType = "membership"

    @Test
    fun `should announce PNMessageResult when PNMessageResult exist in messages`() {
        // given
        val messages: List<PNEvent> = listOf(PNMessageResult(createBasePubSubResult(), JsonPrimitive(message)))
        val emitMessagesEffect = EmitMessagesEffect(messagesConsumer, messages)
        val pnEventCapture = slot<PNMessageResult>()
        every { messagesConsumer.announce(capture(pnEventCapture)) } returns Unit

        // when
        emitMessagesEffect.runEffect()

        // then
        verify(exactly = 1) { messagesConsumer.announce(capture(pnEventCapture)) }
        assertEquals(message, pnEventCapture.captured.message.asString)
    }

    @Test
    fun `should announce PNSignalResult when PNSignalResult exist in messages`() {
        // given
        val messages: List<PNEvent> = listOf(PNSignalResult(createBasePubSubResult(), JsonPrimitive(message)))
        val emitMessagesEffect = EmitMessagesEffect(messagesConsumer, messages)
        val pnEventCapture = slot<PNSignalResult>()
        every { messagesConsumer.announce(capture(pnEventCapture)) } returns Unit

        // when
        emitMessagesEffect.runEffect()

        // then
        verify(exactly = 1) { messagesConsumer.announce(capture(pnEventCapture)) }
        assertEquals(message, pnEventCapture.captured.message.asString)
    }

    @Test
    fun `should announce PNFileEventResult when PNFileEventResult exist in messages`() {
        // given
        val messages: List<PNEvent> = listOf(createPnFileEventResult(message))
        val emitMessagesEffect = EmitMessagesEffect(messagesConsumer, messages)
        val pnEventCapture = slot<PNFileEventResult>()
        every { messagesConsumer.announce(capture(pnEventCapture)) } returns Unit

        // when
        emitMessagesEffect.runEffect()

        // then
        verify(exactly = 1) { messagesConsumer.announce(capture(pnEventCapture)) }
        assertEquals(message, pnEventCapture.captured.jsonMessage.asString)
    }

    @Test
    fun `should announce PNMessageActionResult when PNMessageActionResult exist in messages`() {
        // given
        val messages: List<PNEvent> = listOf(createPnMessageActionResult(messageActionType))
        val emitMessagesEffect = EmitMessagesEffect(messagesConsumer, messages)
        val pnEventCapture = slot<PNMessageActionResult>()
        every { messagesConsumer.announce(capture(pnEventCapture)) } returns Unit

        // when
        emitMessagesEffect.runEffect()

        // then
        verify(exactly = 1) { messagesConsumer.announce(capture(pnEventCapture)) }
        assertEquals(messageActionType, pnEventCapture.captured.data.type)
    }

    @Test
    fun `should announce PNObjectEventResult when PNObjectEventResult exist in messages`() {
        // given
        val messages: List<PNEvent> = listOf(createPnObjectEventResult(objectEventType))
        val emitMessagesEffect = EmitMessagesEffect(messagesConsumer, messages)
        val pnEventCapture = slot<PNObjectEventResult>()
        every { messagesConsumer.announce(capture(pnEventCapture)) } returns Unit

        // when
        emitMessagesEffect.runEffect()

        // then
        verify(exactly = 1) { messagesConsumer.announce(capture(pnEventCapture)) }
        assertEquals(objectEventType, pnEventCapture.captured.extractedMessage.type)
    }

    @Test
    fun `should announce all types of messages when provided`() {
        // given
        val messagesConsumer = CreateMessagesConsumerImpl()
        val basePubSubResult = createBasePubSubResult()
        val pnMessageResult = PNMessageResult(basePubSubResult, JsonPrimitive(message))
        val pnPresenceEventResult = PNPresenceEventResult(channel = "channel")
        val pnFileEventResult = createPnFileEventResult(message)
        val pnSignalResult = PNSignalResult(basePubSubResult, JsonPrimitive("604C7"))
        val pnMessageActionResult = createPnMessageActionResult(messageActionType)
        val pnObjectEventResult = createPnObjectEventResult(objectEventType)

        val listOfDifferentMessages = createListOfDifferentMessages(
            pnMessageResult,
            pnPresenceEventResult,
            pnFileEventResult,
            pnSignalResult,
            pnMessageActionResult,
            pnObjectEventResult
        )
        val emitMessagesEffect = EmitMessagesEffect(messagesConsumer, listOfDifferentMessages)

        // when
        emitMessagesEffect.runEffect()

        // then
        assertTrue(messagesConsumer.pnMessageResultList.contains(pnMessageResult))
        assertTrue(messagesConsumer.pnPresenceEventResultList.contains(pnPresenceEventResult))
        assertTrue(messagesConsumer.pnFileEventResultList.contains(pnFileEventResult))
        assertTrue(messagesConsumer.pnSignalResultList.contains(pnSignalResult))
        assertTrue(messagesConsumer.pnMessageActionResultList.contains(pnMessageActionResult))
        assertTrue(messagesConsumer.pnObjectEventResultList.contains(pnObjectEventResult))
    }

    private fun createListOfDifferentMessages(
        pnMessageResult: PNMessageResult,
        pnPresenceEventResult: PNPresenceEventResult,
        pnFileEventResult: PNFileEventResult,
        pnSignalResult: PNSignalResult,
        pnMessageActionResult: PNMessageActionResult,
        pnObjectEventResult: PNObjectEventResult
    ): List<PNEvent> {
        return listOf(
            pnMessageResult,
            pnPresenceEventResult,
            pnFileEventResult,
            pnSignalResult,
            pnMessageActionResult,
            pnObjectEventResult
        )
    }

    private fun createPnObjectEventResult(objectEventType: String): PNObjectEventResult {
        val pnSetMembershipEvent = PNSetMembershipEvent(
            "ThisIsMyChannelBE8DC5FD86",
            "client-dc55ef8d-2595-4835-9826-53906e9c25fe",
            null,
            "AZO/t53al7m8fw",
            "2023-05-05T14:26:55.824848794Z",
            null
        )
        val pnSetMembershipEventMessage =
            PNSetMembershipEventMessage("objects", "2.0", "set", objectEventType, pnSetMembershipEvent)
        val pnObjectEventResult = PNObjectEventResult(createBasePubSubResult(), pnSetMembershipEventMessage)
        return pnObjectEventResult
    }

    private fun createPnMessageActionResult(messageActionType: String): PNMessageActionResult {
        val pnMessageAction = PNMessageAction(
            messageActionType,
            "\uD83D\uDE07\uD83E\uDD16\uD83E\uDD22\uD83D\uDE1E\uD83D\uDE03",
            16832965735787913
        )
        val pnMessageActionResult = PNMessageActionResult(createBasePubSubResult(), "added", pnMessageAction)
        return pnMessageActionResult
    }

    private fun createPnFileEventResult(message: String): PNFileEventResult {
        val pnDownloadableFile = PNDownloadableFile(
            "99c70a2f-cdaf-49a1-813c-33f7e1aa560f",
            "fileNamech_1683295897374_04C360CB60.txt",
            "https://ps.pndsn.com/v1/files/sub-c-cb8b98b4-cd27-11ec-b360-1a35c262c233/channels/ch_1683295897374_04C360CB60/files/99c70a2f-cdaf-49a1-813c-33f7e1aa560f/fileNamech_1683295897374_04C360CB60.txt"
        )
        val pnFileEventResult = PNFileEventResult(
            "channelFile",
            16832958984018219,
            "client-123",
            "This is message",
            pnDownloadableFile,
            JsonPrimitive(message)
        )
        return pnFileEventResult
    }

    private fun createBasePubSubResult() = BasePubSubResult(
        "channel1",
        "my.*",
        16832048617009353L,
        null,
        "client-c2804687-7d25-4f0b-a442-e3820265b46c"
    )
}

class CreateMessagesConsumerImpl : MessagesConsumer {
    val pnMessageResultList = mutableListOf<PNMessageResult>()
    val pnPresenceEventResultList = mutableListOf<PNPresenceEventResult>()
    val pnSignalResultList = mutableListOf<PNSignalResult>()
    val pnMessageActionResultList = mutableListOf<PNMessageActionResult>()
    val pnObjectEventResultList = mutableListOf<PNObjectEventResult>()
    val pnFileEventResultList = mutableListOf<PNFileEventResult>()

    override fun announce(message: PNMessageResult) {
        pnMessageResultList.add(message)
    }

    override fun announce(presence: PNPresenceEventResult) {
        pnPresenceEventResultList.add(presence)
    }

    override fun announce(signal: PNSignalResult) {
        pnSignalResultList.add(signal)
    }

    override fun announce(messageAction: PNMessageActionResult) {
        pnMessageActionResultList.add(messageAction)
    }

    override fun announce(pnObjectEventResult: PNObjectEventResult) {
        pnObjectEventResultList.add(pnObjectEventResult)
    }

    override fun announce(pnFileEventResult: PNFileEventResult) {
        pnFileEventResultList.add(pnFileEventResult)
    }
}
