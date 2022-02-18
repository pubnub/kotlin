package com.pubnub.api.subscribe.internal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.network.CallsExecutor
import com.pubnub.api.subscribe.internal.Commands.*
import org.junit.Test

class SubscribeModuleInternalsTest {

    @Test
    fun testGlue() {
        val pubnub = PubNub(PNConfiguration("something").apply {
            publishKey = "demo-36"
            subscribeKey = "demo-36"
            logVerbosity = PNLogVerbosity.BODY
        })


        val subscribeModule = SubscribeModuleInternals.create(
            callsExecutor = CallsExecutor(pubnub),
            incomingPayloadProcessor = object : IncomingPayloadProcessor {
                override fun processIncomingPayload(message: SubscribeMessage) {
                    //do nothing
                }
            }
        )

        subscribeModule.handleEvent(SubscribeIssued(channels = listOf("ch1")))

        Thread.sleep(5_000)

        subscribeModule.handleEvent(SubscribeIssued(channels = listOf("ch2")))
        Thread.sleep(5_000)

        subscribeModule.handleEvent(SubscribeIssued(channels = listOf("ch3")))
        Thread.sleep(5_000)

        subscribeModule.handleEvent(SubscribeIssued(channels = listOf("ch4")))
        Thread.sleep(5_000)

        subscribeModule.handleEvent(SubscribeIssued(channels = listOf("ch5", "ch6")))
        Thread.sleep(5_000)
    }
}