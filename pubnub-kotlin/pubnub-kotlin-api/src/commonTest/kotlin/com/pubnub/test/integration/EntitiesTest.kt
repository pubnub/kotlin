package com.pubnub.test.integration

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.kmp.createEventListener
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.test
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals

class EntitiesTest : BaseIntegrationTest() {
    private val channelName = "myChannel"

    @Test
    fun can_subscribe_channel_subscription() = runTest {
        pubnub.test(backgroundScope) {
            val channel = pubnub.channel(channelName)
            val subscription = channel.awaitSubscribe()
            assertEquals(listOf(channelName), pubnub.getSubscribedChannels())
        }
    }

    @Test
    fun can_get_events_from_channel_subscription() = runTest {
        pubnub.test(backgroundScope) {
            val channel = pubnub.channel(channelName)
            val subscription = channel.awaitSubscribe()

            var message: PNMessageResult? = null
            subscription.addListener(
                createEventListener(pubnub, onMessage = { pubNub, pnMessageResult ->
                    message = pnMessageResult
                })
            )

            pubnub.publish(channelName, "some message", shouldStore = false).await()
            yield()

            assertEquals(nextMessage(), message)
        }
    }

    @Test
    fun can_subscribe_channel_subscriptionSet() = runTest {
        pubnub.test(backgroundScope) {
            val channelSet = setOf(channelName, "abc")
            val set = pubnub.subscriptionSetOf(channelSet)
            pubnub.awaitSubscribe(channelSet) {
                set.subscribe()
            }
            assertEquals(channelSet, pubnub.getSubscribedChannels().toSet())
        }
    }

    @Test
    fun can_get_events_from_channel_subscriptionSet() = runTest {
        pubnub.test(backgroundScope) {
            val channelSet = setOf(channelName, "abc")
            val set = pubnub.subscriptionSetOf(channelSet)
            pubnub.awaitSubscribe(channelSet) {
                set.subscribe()
            }
            assertEquals(channelSet, pubnub.getSubscribedChannels().toSet())

            var messageFromSetSubscription: PNMessageResult? = null
            set.addListener(
                createEventListener(pubnub, onMessage = { pubNub, pnMessageResult ->
                    messageFromSetSubscription = pnMessageResult
                })
            )

            pubnub.publish(channelName, "some message", shouldStore = false).await()
            val receivedMessage = nextMessage()
            yield()

            assertEquals(receivedMessage, messageFromSetSubscription)
        }
    }
}
