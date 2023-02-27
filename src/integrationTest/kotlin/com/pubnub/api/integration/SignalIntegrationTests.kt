package com.pubnub.api.integration

import com.google.gson.JsonObject
import com.pubnub.api.SpaceId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.MessageType
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class SignalIntegrationTests : BaseIntegrationTest() {

    private val expectedSpaceId = SpaceId("thisIsSpace")
    private val expectedMessageType = MessageType("thisIsMessageType")
    private val expectedPayload = JsonObject().apply { addProperty("this_is_payload", "value") }

    @Test
    fun grantTest() {
        server.configuration.logVerbosity = PNLogVerbosity.BODY
        server.grant(
            authKeys = listOf("auth1", "auth2"),
            channels = listOf("ch1", "ch2")
        ).sync()
    }

    @Test
    fun sendSignal() {
        pubnub.doWhenSubscribedAndConnected {

            pubnub.signal(
                channel = it.channel,
                message = expectedPayload,
                spaceId = expectedSpaceId,
                messageType = expectedMessageType,
            ).sync()!!

            val signal = it.receivedSignals.pollOrThrow(3_000, TimeUnit.MILLISECONDS)

            MatcherAssert.assertThat(signal.channel, Matchers.`is`(it.channel))
            MatcherAssert.assertThat(signal.messageType, Matchers.`is`(expectedMessageType))
            MatcherAssert.assertThat(signal.spaceId, Matchers.`is`(expectedSpaceId))
            MatcherAssert.assertThat(signal.message, Matchers.`is`(expectedPayload))
        }
    }
}
