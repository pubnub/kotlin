package com.pubnub.contract.step

import com.pubnub.contract.ContractTestConfig
import com.pubnub.contract.state.World
import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

class KeysetStep(private val world: World) {

    @Given("I have a keyset with access manager enabled")
    fun i_have_a_keyset_with_access_manager_enabled() {
        MatcherAssert.assertThat(ContractTestConfig.pamPubKey, Matchers.notNullValue())
        MatcherAssert.assertThat(ContractTestConfig.pamSubKey, Matchers.notNullValue())
        MatcherAssert.assertThat(ContractTestConfig.pamSecKey, Matchers.notNullValue())
        world.configuration.apply {
            subscribeKey = ContractTestConfig.pamSubKey
            publishKey = ContractTestConfig.pamPubKey
            secretKey = ContractTestConfig.pamSecKey
        }
    }

    @Given.Givens(
        Given("I have a keyset with access manager enabled - without secret key"),
        Given("the invalid keyset")
    )
    fun i_have_a_keyset_with_access_manager_enabled_without_secret_key() {
        MatcherAssert.assertThat(ContractTestConfig.pubKey, Matchers.notNullValue())
        MatcherAssert.assertThat(ContractTestConfig.subKey, Matchers.notNullValue())

        world.configuration.apply {
            subscribeKey = ContractTestConfig.subKey
            publishKey = ContractTestConfig.pubKey
        }
    }

    @Given("the demo keyset")
    fun the_demo_keyset() {
        MatcherAssert.assertThat(ContractTestConfig.demoPubKey, Matchers.notNullValue())
        MatcherAssert.assertThat(ContractTestConfig.demoSubKey, Matchers.notNullValue())

        world.configuration.apply {
            subscribeKey = ContractTestConfig.demoSubKey
            publishKey = ContractTestConfig.demoPubKey
        }
    }
}
