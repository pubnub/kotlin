package com.pubnub.contract.subscribe.step

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(private val world: World) {
    @When("I subscribe")
    fun i_subscribe() {
        world.pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                world.statuses.add(pnStatus)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                world.messages.add(pnMessageResult)
            }
        })

        world.pubnub.subscribe(channels = listOf("channel"))
    }
}
