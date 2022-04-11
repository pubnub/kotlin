package com.pubnub.api.subscribe.internal

import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Assert.fail
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SubscribeMachineTest {

    @Test
    fun firstTest() {
        val transition = subscribeTransition()
        val status = PNStatus(
            category = PNStatusCategory.PNBadRequestCategory, error = true, PNOperationType.PNSubscribeOperation
        )

        val events = listOf(
            InitialEvent,
            SubscriptionChanged(channels = listOf("ch1")),
            HandshakingFailure(status),
            HandshakingReconnectingSuccess(cursor = Cursor(timetoken = 5, region = "12")),
            ReceivingFailure(status),
            ReceiveReconnectingSuccess(
                SubscribeEnvelope(
                    messages = listOf(), metadata = SubscribeMetaData(timetoken = 5, region = "12")
                )
            ),
            ReceivingSuccess(
                SubscribeEnvelope(
                    messages = listOf(), metadata = SubscribeMetaData(timetoken = 5, region = "12")
                )
            ),
            HandshakingFailure(status)

        )

        val effects =
            events.fold<SubscribeEvent, Pair<SubscribeState, Collection<SubscribeEffectInvocation>>>(Unsubscribed to listOf()) { acc, ev ->
                transition(acc.first, ev).let { it.first to acc.second + it.second }
            }.second

        println(effects)

        assertThat(
            effects.filterIsInstance<NotificationEffect>(),
            Matchers.`is`(
                listOf(
                    Connected, Reconnected
                )
            )
        )
    }

    @Test
    fun receiveMessages() {
        val latch = CountDownLatch(1)

        val pubnub = PubNub(
            PNConfiguration("not_random_uuid", enableSubscribeBeta = true).apply {
                subscribeKey = Keys.subscribeKey
                publishKey = Keys.publishKey
                logVerbosity = PNLogVerbosity.BODY
            }
        )

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                println(pnStatus)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                println(pnMessageResult)
                latch.countDown()
            }
        })
        pubnub.subscribe(channels = listOf("ch1"))

        Thread.sleep(500)

        pubnub.publish(channel = "ch1", message = "It's alive").sync()

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("The message was not received")
        }
    }
}
