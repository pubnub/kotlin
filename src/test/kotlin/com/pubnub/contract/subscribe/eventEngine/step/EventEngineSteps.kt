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
import java.util.concurrent.TimeUnit

class EventEngineSteps(private val eventEngineState: EventEngineState) {

    @Given("a linear reconnection policy with {int} retries")
    fun a_linear_reconnection_policy_with_retries(maxRetries: Int) {
        eventEngineState.configuration.reconnectionPolicy = PNReconnectionPolicy.LINEAR
        eventEngineState.configuration.maximumReconnectionRetries = maxRetries - 1 // todo figure out who's wrong
    }

    @When("I subscribe")
    fun i_subscribe() {
        eventEngineState.pubnub.configuration.enableSubscribeBeta = true
        eventEngineState.pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                eventEngineState.statusesList.add(pnStatus)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                eventEngineState.messagesList.add(pnMessageResult)
            }

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                eventEngineState.messagesList.add(pnPresenceEventResult)
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                eventEngineState.messagesList.add(pnSignalResult)
            }

            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
                eventEngineState.messagesList.add(pnMessageActionResult)
            }

            override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
                eventEngineState.messagesList.add(objectEvent)
            }

            override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
                eventEngineState.messagesList.add(pnFileEventResult)
            }
        })

        eventEngineState.pubnub.subscribe(channels = listOf(eventEngineState.channelName))
    }

    @Then("I receive the message in my subscribe response")
    fun i_receive_the_message_in_my_subscribe_response() {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).untilAsserted {
            MatcherAssert.assertThat(
                eventEngineState.messagesList.map { it::class.java },
                CoreMatchers.hasItems(PNMessageResult::class.java)
            )
        }
    }

    @Then("I observe the following:")
    fun i_observe_the_following(dataTable: DataTable) {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).untilAsserted {
            val expectedNames = dataTable.asMaps().map { it["type"] to it["name"] }.toList()
            MatcherAssert.assertThat(eventEngineState.queuedElements, Matchers.`is`(expectedNames))
        }
    }

    @Then("I receive an error in my subscribe response")
    fun i_receive_an_error_in_my_subscribe_response() {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).untilAsserted {
            MatcherAssert.assertThat(eventEngineState.statusesList.map { it.error }, CoreMatchers.hasItems(true))
        }
    }
}
