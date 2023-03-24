package com.pubnub.api.integration

import com.google.gson.JsonObject
import com.pubnub.api.SpaceId
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class SignalIntegrationTests : BaseIntegrationTest() {

    private val expectedSpaceId = SpaceId("thisIsSpace")
    private val expectedType = "thisIsType"
    private val expectedPayload = JsonObject().apply { addProperty("this_is_payload", "value") }

    @Test
    fun sendSignal() {
        pubnub.doWhenSubscribedAndConnected {

            pubnub.signal(
                channel = it.channel,
                message = expectedPayload,
                spaceId = expectedSpaceId,
                type = expectedType,
            ).sync()!!

            val signal = it.receivedSignals.pollOrThrow(3_000, TimeUnit.MILLISECONDS)

            MatcherAssert.assertThat(signal.channel, Matchers.`is`(it.channel))
            MatcherAssert.assertThat(signal.type, Matchers.`is`(expectedType))
            MatcherAssert.assertThat(signal.spaceId, Matchers.`is`(expectedSpaceId))
            MatcherAssert.assertThat(signal.message, Matchers.`is`(expectedPayload))
        }
    }
}
