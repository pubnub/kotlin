package com.pubnub.contract.presence.eventEngine.step

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.contract.subscribe.eventEngine.state.EventEngineState
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.callbacks.SubscribeCallback
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.awaitility.Awaitility
import org.awaitility.kotlin.await
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class PresenceEventEngineSteps(private val state: EventEngineState) {

    @Given("heartbeatInterval set to {string}, timeout set to {string} and suppressLeaveEvents set to {string}")
    fun heartbeatInterval_set_timeout_and_suppressLeaveEvents_set(
        heartbeatInterval: String,
        timeout: String,
        suppressLeaveEvents: String
    ) {
        state.configuration.presenceTimeout = timeout.toInt()
        state.configuration.heartbeatInterval = heartbeatInterval.toInt()
        state.configuration.suppressLeaveEvents = suppressLeaveEvents.toBooleanStrict()
    }

    @When("I join {string}, {string}, {string} channels")
    fun I_join_channels(firstChannel: String, secondChannel: String, thirdChannel: String) {
        state.pubnub.pubNubImpl.subscribe(channels = listOf(firstChannel, secondChannel, thirdChannel))
    }

    @When("I join {string}, {string}, {string} channels with presence")
    fun I_join_channels_with_presence(firstChannel: String, secondChannel: String, thirdChannel: String) {
        state.pubnub.pubNubImpl.subscribe(
            channels = listOf(firstChannel, secondChannel, thirdChannel),
            withPresence = true
        )
    }

    @When("The timeout expires")
    fun The_timeout_expires() {
        Thread.sleep(10000)
    }

    @Then("I wait {string} seconds")
    fun I_wait_seconds(waitingTimeInSeconds: String) {
        Thread.sleep(waitingTimeInSeconds.toLong() * 1000)
    }

    @Then("I wait for getting Presence joined events")
    fun I_wait_for_getting_Presence_joined_events() {
        val atomic = AtomicInteger(0)
        state.pubnub.addListener(object : SubscribeCallback {
            override fun status(pubnub: BasePubNub, pnStatus: PNStatus) {
                // do nothing
            }

            override fun presence(pubnub: BasePubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "join" && (pnPresenceEventResult.channel == "first" || pnPresenceEventResult.channel == "second" || pnPresenceEventResult.channel == "third")) {
                    atomic.incrementAndGet()
                }
            }
        })

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
            .untilAtomic(atomic, IsEqual.equalTo(3))
    }

    @Then("I receive an error in my heartbeat response")
    fun I_receive_an_error_in_my_heartbeat_response() {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(500, TimeUnit.MILLISECONDS).untilAsserted {
            val expectedNames = "event" to "HEARTBEAT_FAILURE"
            MatcherAssert.assertThat(state.presenceQueuedElements, hasItem(expectedNames))
        }
    }

    @Then("I leave {string} and {string} channels")
    fun I_leave_channels(firstChannel: String, secondChannel: String) {
        state.pubnub.pubNubImpl.unsubscribe(channels = listOf(firstChannel, secondChannel))
    }

    @Then("I observe the following Events and Invocations of the Presence EE:")
    fun i_observe_the_following(dataTable: DataTable) {
        await.pollInterval(50, TimeUnit.MILLISECONDS).atMost(5000, TimeUnit.MILLISECONDS).untilAsserted {
            if (state.configuration.suppressLeaveEvents) {
                // when suppressLeaveEvents is true in Kotlin SDK we do invocation LEAVE, but it will not execute LeaveEffect
                // other SDK don't do invocation LEAVE and cucumber test doesn't expect LEAVE invocation.
                // In Kotlin SDK we have EventEngine.performTransitionAndEmitEffects method that is
                // common for Presence EE and Subscribe EE. I don't want to pollute this method with if statement that check
                // if suppressLeaveEvents is true. That's why I remove LEAVE invocation from the queue.
                state.presenceQueuedElements.remove("invocation" to "LEAVE")
            }
            val expectedNames = dataTable.asMaps().map { it["type"] to it["name"] }.toList()
            MatcherAssert.assertThat(state.presenceQueuedElements, Matchers.`is`(expectedNames))
        }
    }

    @Then("I don't observe any Events and Invocations of the Presence EE")
    fun i_dont_observe_any_events_and_invocations_of_the_presence_ee() {
        Thread.sleep(3000)
        assertTrue(state.presenceQueuedElements.isEmpty())
    }

    @Then("I leave {string} and {string} channels with presence")
    fun I_leave_channels_with_presence(firstChannel: String, secondChannel: String) {
        state.pubnub.pubNubImpl.unsubscribe(channels = listOf(firstChannel, secondChannel))
    }
}
