package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test


class SubscribeMachineTest {

    @Test
    fun firstTest() {
        val module = subscribeMachine(shouldRetry = { it < 1 })

        val inputs = listOf(
            InitialEvent,
            Commands.SubscribeIssued(channels = listOf("ch1")),
            HandshakeResult.HandshakeFailed,
            HandshakeResult.HandshakeSucceeded(cursor = Cursor(timetoken = 5, region = "12")),
            ReceivingResult.ReceivingFailed,
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
            HandshakeResult.HandshakeFailed

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