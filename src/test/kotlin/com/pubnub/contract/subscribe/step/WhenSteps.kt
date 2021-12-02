package com.pubnub.contract.subscribe.step

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.contract.state.World
import io.cucumber.java.PendingException
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