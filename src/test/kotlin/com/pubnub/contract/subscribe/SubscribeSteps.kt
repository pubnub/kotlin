package com.pubnub.contract.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.MessageResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.contract.state.World
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.awaitility.kotlin.await
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasItems
import java.util.concurrent.TimeUnit

class SubscribeSteps(
    private val subscribeState: SubscribeState,
    private val world: World
) {

    @When("I subscribe to {string} channel")
    fun i_subscribe_to_channel(channel: String) {
        world.pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                subscribeState.messagesList.add(pnMessageResult)
            }

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                subscribeState.messagesList.add(pnPresenceEventResult)
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                subscribeState.messagesList.add(pnSignalResult)
            }

            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
                subscribeState.messagesList.add(pnMessageActionResult)
            }

            override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
                subscribeState.messagesList.add(objectEvent)
            }

            override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
                subscribeState.messagesList.add(pnFileEventResult)
            }
        })
        world.pubnub.subscribe(channels = listOf(channel))
    }

    @Then("I receive {int} messages in my subscribe response")
    fun i_receive_messages_in_my_subscribe_response(numberOfMessages: Int) {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).until {
            subscribeState.messagesList.size >= numberOfMessages
        }
    }

    @Then("response contains messages with {string} and {string} types")
    fun response_contains_messages_with_and_message_types(
        firstType: String,
        secondType: String
    ) {
        assertThat(
            subscribeState.messagesList.mapNotNull {
                when (it) {
                    is MessageResult -> {
                        it.type
                    }

                    else -> null
                }
            },
            hasItems(firstType, secondType)
        )
    }

    @Then("response contains messages without space ids")
    fun response_contains_messages_without_space_ids() {
        assertThat(
            subscribeState.messagesList.mapNotNull {
                when (it) {
                    is MessageResult -> {
                        it.spaceId
                    }

                    else -> null
                }
            },
            empty()
        )
    }

    @Then("response contains messages with space ids")
    fun response_contains_messages_with_space_ids() {
        assertThat(
            subscribeState.messagesList.map {
                when (it) {
                    is MessageResult -> {
                        it.spaceId
                    }

                    else -> null
                }
            },
            hasItems(Matchers.notNullValue())
        )
    }
}
