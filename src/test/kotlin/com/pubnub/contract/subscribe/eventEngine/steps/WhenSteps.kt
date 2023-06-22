package com.pubnub.contract.subscribe.eventEngine.steps

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.contract.state.EventEngineConfTestImpl
import com.pubnub.contract.state.World
import com.pubnub.contract.subscribe.eventEngine.state.EventEngineState
import io.cucumber.java.en.When
import org.junit.jupiter.api.fail
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class WhenSteps(

    private val world: World,
    private val eventEngineState: EventEngineState
) {

    @When("I subscribe")
    fun I_subscribe() {
        val pnConfiguration = world.configuration
        world.pubnub = PubNub(pnConfiguration, EventEngineConfTestImpl(eventEngineState.queuedElements))
        world.pubnub.configuration.enableSubscribeBeta = true
        val connected = CountDownLatch(1)

        world.pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.category == PNStatusCategory.PNConnectedCategory) {
                    connected.countDown()
                }
            }
        })

        world.pubnub.subscribe(channels = listOf(eventEngineState.channelName))

        if (!connected.await(5, TimeUnit.SECONDS)) {
            fail("Didn't connect")
        }
    }

    @When("I publish a message")
    fun I_publish_a_message() {
        val messageReceived = CountDownLatch(1)
        world.pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                eventEngineState.responseMessage = pnMessageResult.message.toString()
                messageReceived.countDown()
            }
        })

        world.pubnub.publish(channel = eventEngineState.channelName, message = "Any message because mock server returns hello").sync()

        if (!messageReceived.await(5, TimeUnit.SECONDS)) {
            fail("Didn't receive")
        }
    }
}
