package com.pubnub.contract.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.contract.state.World
import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SubscribeSteps(private val world: World) {

    @Given("a linear reconnection policy with {int} retries")
    fun a_linear_reconnection_policy_with_retries(retries: Int) {
        world.pubnub.configuration.reconnectionPolicy = PNReconnectionPolicy.LINEAR
        world.pubnub.configuration.maximumReconnectionRetries = retries
    }

    @When("I subscribe")
    fun i_subscribe() {
        val countDownLatch = CountDownLatch(1)
        world.pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                countDownLatch.countDown()
            }
        })
        world.pubnub.subscribe(channels = listOf("whatever"))

        countDownLatch.await(5_000, TimeUnit.MILLISECONDS)
    }
    @When("I publish a message")
    fun i_publish_a_message() {
        world.pubnub.publish(channel = "whatever", message = "Whatever").sync()
    }
    @Then("I receive the message in my subscribe response")
    fun i_receive_the_message_in_my_subscribe_response() {
        Thread.sleep(500)
    }
    @Then("I observe the following:")
    fun i_observe_the_following(data: List<List<String>>) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        throw PendingException()
    }
}
