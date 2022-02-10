package com.pubnub.api.subscribe.internal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.network.CallsExecutor
import com.pubnub.api.subscribe.SInput
import org.junit.Test
import java.util.concurrent.LinkedBlockingQueue

class SubscribeModuleTest {

    @Test
    fun testGlue() {
        val pubnub = PubNub(PNConfiguration("something").apply {
            publishKey = "demo-36"
            subscribeKey = "demo-36"
            logVerbosity = PNLogVerbosity.BODY
        })

        val inputQueue = LinkedBlockingQueue<SInput>(100)

        val subscribeModule = SubscribeModule(
            inputQueue = inputQueue,
            callsExecutor = CallsExecutor(pubnub)
        )

        subscribeModule.run()

        inputQueue.put(SubscribeInput(channels = listOf("ch1")))

        Thread.sleep(30_000)

        inputQueue.put(SubscribeInput(channels = listOf("ch2")))
        Thread.sleep(5_000)

        inputQueue.put(SubscribeInput(channels = listOf("ch3")))
        Thread.sleep(5_000)

        inputQueue.put(SubscribeInput(channels = listOf("ch4")))
        Thread.sleep(5_000)

        inputQueue.put(SubscribeInput(channels = listOf("ch5", "ch6")))
        Thread.sleep(5_000)
    }
}