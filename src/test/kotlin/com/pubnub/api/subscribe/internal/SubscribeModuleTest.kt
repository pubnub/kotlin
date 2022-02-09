package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import org.junit.Test


class SubscribeModuleTest {

    @Test
    fun firstTest() {
        val module = SubscribeModule()

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

        println(effects)
    }

}