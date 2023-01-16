package com.pubnub.api.workers

import com.google.gson.JsonPrimitive
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.SpaceId
import com.pubnub.api.UserId
import com.pubnub.api.managers.DuplicationManager
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.server.PublishMetaData
import com.pubnub.api.models.server.SubscribeMessage
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test
import org.hamcrest.core.Is.`is` as iz

class SubscribeMessageProcessorTest {

    @Test
    fun pnMessageResultWillContainSpaceIdIfItsSetInSubscribeMessage() {
        val expectedSpaceId = SpaceId("spaceId")
        val subscribeMessage = subscribeMessage().copy(
            spaceIdString = expectedSpaceId.value
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).spaceId, iz(expectedSpaceId))
    }

    @Test
    fun pnMessageResultWillContainMessageTypeIfItsSetInSubscribeMessage() {
        val expectedMessageType = MessageType("messageType")
        val subscribeMessage = subscribeMessage().copy(
            userMessageTypeString = expectedMessageType.value
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).messageType, iz(expectedMessageType))
    }

    @Test
    fun pnMessageResultWillContainMessageMessageTypeIfItsNotSetInSubscribeMessage() {
        val subscribeMessage = subscribeMessage().copy(
            pnMessageTypeInt = 0,
            userMessageTypeString = null
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).messageType, iz(MessageType.Message))
    }

    @Test
    fun pnSignalResultWillContainSpaceIdIfItsSetInSubscribeMessage() {
        val expectedSpaceId = SpaceId("spaceId")
        val subscribeMessage = subscribeMessage().copy(
            pnMessageTypeInt = 1,
            spaceIdString = expectedSpaceId.value
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).spaceId, iz(expectedSpaceId))
    }

    @Test
    fun pnSignalResultWillContainMessageTypeIfItsSetInSubscribeMessage() {
        val expectedMessageType = MessageType("messageType")
        val subscribeMessage = subscribeMessage().copy(
            pnMessageTypeInt = 1,
            userMessageTypeString = expectedMessageType.value
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).messageType, iz(expectedMessageType))
    }

    @Test
    fun pnSignalResultWillContainSignalMessageTypeIfItsNotSetInSubscribeMessage() {
        val subscribeMessage = subscribeMessage().copy(
            pnMessageTypeInt = 1,
            userMessageTypeString = null
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).messageType, iz(MessageType.Signal))
    }

    private fun subscribeMessageProcessor(): SubscribeMessageProcessor {
        val pubnub = PubNub(
            PNConfiguration(UserId("userId")).apply {
                subscribeKey = "subKey"
                publishKey = "pubKey"
            }
        )

        val duplicationManager = DuplicationManager(pubnub.configuration)

        return SubscribeMessageProcessor(
            pubnub, duplicationManager
        )
    }

    private fun subscribeMessage() = SubscribeMessage(
        shard = "4",
        flags = "0",
        publishMetaData = PublishMetaData(16710463855524468, 21),
        channel = "testChannel",
        userMessageTypeString = null,
        spaceIdString = null,
        pnMessageTypeInt = null,
        subscriptionMatch = null,
        issuingClientId = null,
        subscribeKey = "demo",
        originationMetadata = null,
        payload = JsonPrimitive("message"),
        userMetadata = null
    )
}
