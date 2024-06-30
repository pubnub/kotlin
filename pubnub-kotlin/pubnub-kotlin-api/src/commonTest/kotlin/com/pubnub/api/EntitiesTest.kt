package com.pubnub.api

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.kmp.createEventListener
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals

class EntitiesTest : BaseIntegrationTest() {
    private val channelName = "myChannel"

    @Test
    fun can_create_channel_subscription() = runTest(timeout = defaultTimeout) {
        pubnub.test(backgroundScope) {
            val channel = pubnub.channel(channelName)
            val subscription = channel.awaitSubscribe()
            assertEquals(listOf(channelName), pubnub.getSubscribedChannels())
        }
    }

    @Test
    fun can_get_events_from_channel_subscription() = runTest(timeout = defaultTimeout) {
        pubnub.test(backgroundScope) {
            val channel = pubnub.channel(channelName)
            val subscription = channel.awaitSubscribe()

            var message: PNMessageResult? = null
            subscription.addListener(createEventListener(pubnub, onMessage = { pubNub, pnMessageResult ->
                message = pnMessageResult
            }))

            pubnub.publish(channelName, "some message", shouldStore = false).await()
            yield()

            assertEquals(nextMessage(), message)
        }
    }
}