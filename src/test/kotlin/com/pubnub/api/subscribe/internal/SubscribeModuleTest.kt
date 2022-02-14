package com.pubnub.api.subscribe.internal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.network.CallsExecutor
import com.pubnub.api.subscribe.SubscribeInput
import com.pubnub.api.subscribe.SubscribeCommands.*
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

        val inputQueue = LinkedBlockingQueue<SubscribeInput>(100)

        val subscribeModule = SubscribeModule(
            inputQueue = inputQueue,
            callsExecutor = CallsExecutor(pubnub)
        )

        subscribeModule.run()

        inputQueue.put(Subscribe(channels = listOf("ch1")))

        Thread.sleep(5_000)

        inputQueue.put(Subscribe(channels = listOf("ch2")))
        Thread.sleep(5_000)

        inputQueue.put(Subscribe(channels = listOf("ch3")))
        Thread.sleep(5_000)

        inputQueue.put(Subscribe(channels = listOf("ch4")))
        Thread.sleep(5_000)

        inputQueue.put(Subscribe(channels = listOf("ch5", "ch6")))
        Thread.sleep(5_000)
    }
}