package com.pubnub.api.subscribe.internal

import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.subscribe.NewSubscribeModule
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.junit.Ignore

class InternalSubscribeModuleTest {

    @Test
    @Ignore
    fun testGlue() {
        val pubnub = PubNub(
            PNConfiguration("something").apply {
                publishKey = Keys.publishKey
                subscribeKey = Keys.subscribeKey
                logVerbosity = PNLogVerbosity.BODY
            }
        )

        val subscribeModule = NewSubscribeModule.create(
            pubnub = pubnub,
            incomingPayloadProcessor = object : IncomingPayloadProcessor {
                override fun processIncomingPayload(message: SubscribeMessage) {
                    // do nothing
                }
            },
            listenerManager = ListenerManager(pubnub)
        )

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {
                val ch = listOf("ch1", "ch2", "ch3", "ch4", "ch5", "ch6").random()
                pubnub.publish(
                    channel = ch,
                    message = "message"
                ).sync()
            },
            1000, 1000, TimeUnit.MILLISECONDS
        )

        subscribeModule.subscribe(
            channels = listOf("ch1"),
            channelGroups = listOf(),
            withPresence = true,
            withTimetoken = 0
        )

        Thread.sleep(5_000)

        subscribeModule.subscribe(
            channels = listOf("ch2"),
            channelGroups = listOf(),
            withPresence = true,
            withTimetoken = 0
        )
        Thread.sleep(5_000)

        subscribeModule.subscribe(
            channels = listOf("ch3"),
            channelGroups = listOf(),
            withPresence = true,
            withTimetoken = 0
        )
        Thread.sleep(5_000)

        subscribeModule.subscribe(
            channels = listOf("ch4"),
            channelGroups = listOf(),
            withPresence = true,
            withTimetoken = 0
        )
        Thread.sleep(5_000)

        subscribeModule.subscribe(
            channels = listOf("ch5", "ch6"),
            channelGroups = listOf(),
            withPresence = true,
            withTimetoken = 0
        )
        Thread.sleep(5_000)
    }
}
