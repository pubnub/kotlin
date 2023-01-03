package com.pubnub.api.workers

import com.google.gson.JsonPrimitive
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.SpaceId
import com.pubnub.api.UserId
import com.pubnub.api.managers.DuplicationManager
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
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
            stringSpaceId = expectedSpaceId.value
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
    fun pnMessageResultWillNotContainMessageTypeIfItsNotSetInSubscribeMessage() {
        val expectedMessageType = MessageType("messageType")
        val subscribeMessage = subscribeMessage().copy(
            pnMessageTypeInt = 0,
            userMessageTypeString = null
        )

        val result = subscribeMessageProcessor().processIncomingPayload(subscribeMessage)
        assertThat(result, Matchers.instanceOf(PNMessageResult::class.java))
        assertThat((result as PNMessageResult).messageType, iz(nullValue()))
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
        stringSpaceId = null,
        pnMessageTypeInt = null,
        subscriptionMatch = null,
        issuingClientId = null,
        subscribeKey = "demo",
        originationMetadata = null,
        payload = JsonPrimitive("message"),
        userMetadata = null
    )
}
