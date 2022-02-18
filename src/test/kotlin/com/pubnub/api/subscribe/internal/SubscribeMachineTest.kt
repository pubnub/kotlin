package com.pubnub.api.subscribe.internal

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test


class SubscribeMachineTest {

    @Test
    fun firstTest() {
        val module = subscribeMachine(shouldRetry = { it < 1 })
        val status = PNStatus(category = PNStatusCategory.PNBadRequestCategory, error = true, PNOperationType.PNSubscribeOperation)

        val inputs = listOf(
            InitialEvent,
            Commands.SubscribeIssued(channels = listOf("ch1")),
            HandshakeResult.HandshakeFailed(status),
            HandshakeResult.HandshakeSucceeded(cursor = Cursor(timetoken = 5, region = "12")),
            ReceivingResult.ReceivingFailed(status),
            ReceivingResult.ReceivingSucceeded(
                SubscribeEnvelope(
                    messages = listOf(),
                    metadata = SubscribeMetaData(timetoken = 5, region = "12")
                )
            ),
            ReceivingResult.ReceivingSucceeded(
                SubscribeEnvelope(
                    messages = listOf(),
                    metadata = SubscribeMetaData(timetoken = 5, region = "12")
                )
            ),
            HandshakeResult.HandshakeFailed(status)

        )

        val effects = inputs.flatMap {
            module(it)
        }

        println(effects)

        assertThat(
            effects.mapNotNull { if (it is NewState) it.name else null },
            Matchers.`is`(
                listOf(
                    Handshaking::class.simpleName,
                    Receiving::class.simpleName,
                    Receiving::class.simpleName
                )
            )
        )
    }

}