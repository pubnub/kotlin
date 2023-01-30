package com.pubnub.contract.presence

import com.pubnub.api.models.consumer.presence.PNHereNowChannelData
import com.pubnub.contract.state.World
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.`is` as iz

class PresenceSteps(
    private val world: World,
    private val presenceState: PresenceState
) {

    @When("I fetch presence state for {string} channel")
    fun i_fetch_presence_state_for_channel(channel: String) {

        world.pubnub.getPresenceState(channels = listOf(channel)).sync()?.let {
            presenceState.getStateResult = it
            world.responseStatus = 200
        }
    }

    @When("I set state on {string} channel with withHeartbeat set to {boolean}")
    fun i_set_state_on_channel_with_with_heartbeat_set_to(channel: String, heartbeat: Boolean) {
        if (heartbeat == true) {
            throw RuntimeException("Set state by heartbeat is not supported yet")
        }

        world.pubnub.setPresenceState(
            channels = listOf(channel), state = "state"
        ).sync()?.also {
            presenceState.setStateResult = it
            world.responseStatus = 200
        }
    }

    @When("I fetch presence on {string} channel with presence state")
    fun i_fetch_presence_on_channel_with_presence_state(channel: String) {
        world.pubnub.hereNow(
            channels = listOf(channel), includeState = true
        ).sync()?.also {
            presenceState.hereNowResult = it
            world.responseStatus = 200
        }
    }

    @Then("there is no active clients")
    fun there_is_no_active_clients() {
        val ignoreChannelName = ""
        val emptyHereNow = PNHereNowChannelData(channelName = ignoreChannelName, occupancy = 0, occupants = listOf())
        assertThat(presenceState.hereNowResult?.totalOccupancy, iz(0))
        assertThat(
            presenceState.hereNowResult?.channels?.values?.map { it.copy(channelName = ignoreChannelName) },
            iz(listOf(emptyHereNow))
        )
    }

    @Then("there is active clients")
    fun there_is_active_clients() {
        val ignoreChannelName = ""
        val emptyHereNow = PNHereNowChannelData(channelName = ignoreChannelName, occupancy = 0, occupants = listOf())
        assertThat(presenceState.hereNowResult?.totalOccupancy, iz(not(0)))
        assertThat(
            presenceState.hereNowResult?.channels?.values?.map { it.copy(channelName = ignoreChannelName) },
            Matchers.contains(not(emptyHereNow))
        )
    }

    @Then("state associated with active clients")
    fun state_associated_with_active_clients() {
        assertThat(
            presenceState.hereNowResult?.channels?.values?.flatMap { it.occupants }?.map { it.state },
            allOf(notNullValue())
        )
    }

    @Then("response contains state")
    fun response_contains_state() {
        assertThat(presenceState.getStateResult, iz(notNullValue()))
    }
}
