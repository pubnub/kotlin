package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import com.pubnub.api.state.NewStateEffect
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test


class SubscribeMachineTest {

    @Test
    fun firstTest() {
        val module = SubscribeMachine()

        val inputs = listOf(
            SubscribeInput(channels = listOf("ch1")),
            HandshakeResult.HandshakeSuccess(cursor = Cursor(timetoken = 5, region = "12")),
            ReceivingResult.ReceivingSuccess(
                SubscribeEnvelope(
                    messages = listOf(),
                    metadata = SubscribeMetaData(timetoken = 5, region = "12")
                )
            )
        )

        val effects = inputs.flatMap {
            module.handle(it)
        }

        assertThat(effects.mapNotNull { if (it is NewStateEffect) it.name else null }, Matchers.`is`(listOf(SubscribeStates.Handshaking::class.java.name, SubscribeStates.Receiving::class.java.name, SubscribeStates.Receiving::class.java.name)))
    }

}