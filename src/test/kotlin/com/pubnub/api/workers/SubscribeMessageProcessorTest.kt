package com.pubnub.api.workers

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.SpaceId
import com.pubnub.api.UserId
import com.pubnub.api.managers.DuplicationManager
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.server.PublishMetaData
import com.pubnub.api.models.server.SubscribeMessage
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.nullValue
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
    fun pnMessageResultWillContainTypeIfItsSetInSubscribeMessage() {
        val expectedType = "type"
        val subscribeMessage = subscribeMessage().copy(
            type = expectedType
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).type, iz(expectedType))
    }

    @Test
    fun pnMessageResultWillNotContainTypeIfItsNotSetInSubscribeMessage() {
        val subscribeMessage = subscribeMessage().copy(
            messageTypeInt = 0,
            type = null
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).type, iz(nullValue()))
    }

    @Test
    fun pnSignalResultWillContainSpaceIdIfItsSetInSubscribeMessage() {
        val expectedSpaceId = SpaceId("spaceId")
        val subscribeMessage = subscribeMessage().copy(
            messageTypeInt = 1,
            spaceIdString = expectedSpaceId.value
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).spaceId, iz(expectedSpaceId))
    }

    @Test
    fun pnSignalResultWillContainTypeIfItsSetInSubscribeMessage() {
        val expectedType = "type"
        val subscribeMessage = subscribeMessage().copy(
            messageTypeInt = 1,
            type = expectedType
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).type, iz(expectedType))
    }

    @Test
    fun pnSignalResultWillNotContainTypeIfItsNotSetInSubscribeMessage() {
        val subscribeMessage = subscribeMessage().copy(
            messageTypeInt = 1,
            type = null
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNSignalResult::class.java))
        assertThat((result as PNSignalResult).type, iz(nullValue()))
    }

    @Test
    fun pnFileResultWillContainSpaceIdIfItsSetInSubscribeMessage() {
        val expectedSpaceId = SpaceId("spaceId")
        val subscribeMessage = subscribeMessage().copy(
            spaceIdString = expectedSpaceId.value,
            messageTypeInt = 4,
            payload = filePayload()
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNFileEventResult::class.java))
        assertThat((result as PNFileEventResult).spaceId, iz(expectedSpaceId))
    }

    @Test
    fun pnFileResultWillContainMessageTypeIfItsSetInSubscribeMessage() {
        val expectedType = "type"
        val subscribeMessage = subscribeMessage().copy(
            type = expectedType,
            messageTypeInt = 4,
            payload = filePayload()
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNFileEventResult::class.java))
        assertThat((result as PNFileEventResult).type, iz(expectedType))
    }

    @Test
    fun pnFileResultWillNotContainTypeIfItsNotSetInSubscribeMessage() {
        val subscribeMessage = subscribeMessage().copy(
            messageTypeInt = 4,
            payload = filePayload(),
            type = null
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNFileEventResult::class.java))
        assertThat((result as PNFileEventResult).type, iz(nullValue()))
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
        type = null,
        spaceIdString = null,
        messageTypeInt = null,
        subscriptionMatch = null,
        issuingClientId = null,
        subscribeKey = "demo",
        originationMetadata = null,
        payload = JsonPrimitive("message"),
        userMetadata = null
    )

    private fun filePayload() = JsonObject().apply {
        add(
            "file",
            JsonObject().apply {
                addProperty("id", "id")
                addProperty("name", "name")
            }
        )
        addProperty("message", "Message")
    }
}
