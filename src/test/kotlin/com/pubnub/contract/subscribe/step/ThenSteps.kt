package com.pubnub.contract.subscribe.step

import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.contract.state.World
import io.cucumber.java.PendingException
import io.cucumber.java.en.Then
import java.time.Duration
import junit.framework.Assert.assertEquals
import org.awaitility.kotlin.await
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.hasSize

class ThenSteps(private val world: World) {
    @Then("I receive the message in my subscribe response")
    fun i_receive_the_message_in_my_subscribe_response() {
        await.atMost(Duration.ofSeconds(2)).untilAsserted {
            assertThat(world.messages, hasSize(greaterThanOrEqualTo(1)))
        }
    }

    @Then("an error is thrown")
    fun an_error_is_thrown() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @Then("I receive access denied status")
    fun i_receive_access_denied_status() {
        await.atMost(Duration.ofSeconds(2)).untilAsserted {
            assertEquals(PNStatusCategory.PNAccessDeniedCategory, world.statuses.map { it.category }.last())
        }
    }

    @Then("I don't auto-retry subscribe")
    fun i_don_t_auto_retry_subscribe() {
        world.failOnPending = false
        Thread.sleep(2_000)
    }
}

