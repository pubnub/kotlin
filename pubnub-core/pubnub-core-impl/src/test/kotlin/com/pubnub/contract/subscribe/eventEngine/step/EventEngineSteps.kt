package com.pubnub.contract.subscribe.eventEngine.step

import com.pubnub.api.BasePubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.contract.subscribe.eventEngine.state.EventEngineState
import com.pubnub.internal.callbacks.SubscribeCallback
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.awaitility.kotlin.await
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds

class EventEngineSteps(private val state: EventEngineState) {
    @Given("a linear reconnection policy with {int} retries")
    fun a_linear_reconnection_policy_with_retries(maxRetries: Int) {
        state.configuration.retryConfiguration =
            RetryConfiguration.Linear(delayInSec = 1.milliseconds, maxRetryNumber = maxRetries, isInternal = true)
    }

    @When("I subscribe")
    fun i_subscribe() {
        state.pubnub.addListener(
            object : SubscribeCallback {
                override fun status(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnStatus: PNStatus,
                ) {
                    state.statusesList.add(pnStatus)
                }

                override fun message(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnMessageResult: PNMessageResult,
                ) {
                    state.messagesList.add(pnMessageResult)
                }

                override fun presence(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnPresenceEventResult: PNPresenceEventResult,
                ) {
                    state.messagesList.add(pnPresenceEventResult)
                }

                override fun signal(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnSignalResult: PNSignalResult,
                ) {
                    state.messagesList.add(pnSignalResult)
                }

                override fun messageAction(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnMessageActionResult: PNMessageActionResult,
                ) {
                    state.messagesList.add(pnMessageActionResult)
                }

                override fun objects(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    objectEvent: PNObjectEventResult,
                ) {
                    state.messagesList.add(objectEvent)
                }

                override fun file(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnFileEventResult: PNFileEventResult,
                ) {
                    state.messagesList.add(pnFileEventResult)
                }
            },
        )

        state.pubnub.corePubNubClient.subscribe(channels = listOf(state.channelName))
    }

    @When("I subscribe with timetoken {long}")
    fun i_subscribe_with_timetoken(timetoken: Long) {
        state.pubnub.addListener(
            object : SubscribeCallback {
                override fun status(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnStatus: PNStatus,
                ) {
                    state.statusesList.add(pnStatus)
                }

                override fun message(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnMessageResult: PNMessageResult,
                ) {
                    state.messagesList.add(pnMessageResult)
                }

                override fun presence(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnPresenceEventResult: PNPresenceEventResult,
                ) {
                    state.messagesList.add(pnPresenceEventResult)
                }

                override fun signal(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnSignalResult: PNSignalResult,
                ) {
                    state.messagesList.add(pnSignalResult)
                }

                override fun messageAction(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnMessageActionResult: PNMessageActionResult,
                ) {
                    state.messagesList.add(pnMessageActionResult)
                }

                override fun objects(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    objectEvent: PNObjectEventResult,
                ) {
                    state.messagesList.add(objectEvent)
                }

                override fun file(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnFileEventResult: PNFileEventResult,
                ) {
                    state.messagesList.add(pnFileEventResult)
                }
            },
        )

        state.pubnub.corePubNubClient.subscribe(
            channels = listOf(state.channelName),
            withTimetoken = timetoken,
        )
    }

    @Then("I receive the message in my subscribe response")
    fun i_receive_the_message_in_my_subscribe_response() {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(2, TimeUnit.SECONDS).untilAsserted {
            MatcherAssert.assertThat(
                state.messagesList.map { it::class.java },
                CoreMatchers.hasItems(PNMessageResult::class.java),
            )
        }
    }

    @Then("I observe the following:")
    fun i_observe_the_following(dataTable: DataTable) {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).untilAsserted {
            val expectedNames = dataTable.asMaps().map { it["type"] to it["name"] }.toList()
            MatcherAssert.assertThat(state.subscribeQueuedElements, Matchers.`is`(expectedNames))
        }
    }

    @Then("I receive an error in my subscribe response")
    fun i_receive_an_error_in_my_subscribe_response() {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(2, TimeUnit.SECONDS).untilAsserted {
            MatcherAssert.assertThat(state.statusesList.map { it.error }, CoreMatchers.hasItems(true))
        }
    }
}
