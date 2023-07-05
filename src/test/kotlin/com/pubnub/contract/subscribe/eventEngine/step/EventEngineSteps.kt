package com.pubnub.contract.subscribe.eventEngine.step

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.contract.subscribe.eventEngine.state.EventEngineState
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.awaitility.kotlin.await
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import java.time.Duration
import java.util.concurrent.TimeUnit

class EventEngineSteps(private val state: EventEngineState) {

    @Given("a linear reconnection policy with {int} retries")
    fun a_linear_reconnection_policy_with_retries(maxRetries: Int) {
        state.configuration.linearReconnectionDelay = Duration.ofMillis(1)
        state.configuration.reconnectionPolicy = PNReconnectionPolicy.LINEAR
        state.configuration.maximumReconnectionRetries = maxRetries
    }

    @When("I subscribe")
    fun i_subscribe() {
        state.pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                state.statusesList.add(pnStatus)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                state.messagesList.add(pnMessageResult)
            }

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                state.messagesList.add(pnPresenceEventResult)
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                state.messagesList.add(pnSignalResult)
            }

            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
                state.messagesList.add(pnMessageActionResult)
            }

            override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
                state.messagesList.add(objectEvent)
            }

            override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
                state.messagesList.add(pnFileEventResult)
            }
        })

        state.pubnub.subscribe(channels = listOf(state.channelName))
    }

    @Then("I receive the message in my subscribe response")
    fun i_receive_the_message_in_my_subscribe_response() {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).untilAsserted {
            MatcherAssert.assertThat(
                state.messagesList.map { it::class.java },
                CoreMatchers.hasItems(PNMessageResult::class.java)
            )
        }
    }

    @Then("I observe the following:")
    fun i_observe_the_following(dataTable: DataTable) {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).untilAsserted {
            val expectedNames = dataTable.asMaps().map { it["type"] to it["name"] }.toList()
            MatcherAssert.assertThat(state.queuedElements, Matchers.`is`(expectedNames))
        }
    }

    @Then("I receive an error in my subscribe response")
    fun i_receive_an_error_in_my_subscribe_response() {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).untilAsserted {
            MatcherAssert.assertThat(state.statusesList.map { it.error }, CoreMatchers.hasItems(true))
        }
    }
}
