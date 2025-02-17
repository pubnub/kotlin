package com.pubnub.contract.step

import com.pubnub.contract.ContractTestConfig
import com.pubnub.contract.state.World
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

    @Given("I have a keyset with access manager enabled - without secret key")
    fun i_have_a_keyset_with_access_manager_enabled_without_secret_key() {
        MatcherAssert.assertThat(ContractTestConfig.pubKey, Matchers.notNullValue())
        MatcherAssert.assertThat(ContractTestConfig.subKey, Matchers.notNullValue())

        world.configuration.apply {
            subscribeKey = ContractTestConfig.subKey
            publishKey = ContractTestConfig.pubKey
        }
    }

    @Given("I have a keyset with Objects V2 enabled")
    fun i_have_a_keyset_with_objects_v2_enabled() {
        MatcherAssert.assertThat(ContractTestConfig.subKey, Matchers.notNullValue())
        world.configuration.apply {
            subscribeKey = ContractTestConfig.subKey
        }
    }

    @Given("the demo keyset with event engine enabled")
    fun the_demo_keyset_with_event_engine_enabled() {
        i_have_a_keyset_with_access_manager_enabled_without_secret_key()
    }

    @Given("the demo keyset with Presence EE enabled")
    fun the_demo_keyset_with_Presence_EE_enabled() {
        the_demo_keyset_with_event_engine_enabled()
    }

    @Given("the demo keyset")
    fun the_demo_keyset() {
        MatcherAssert.assertThat(ContractTestConfig.pubKey, Matchers.notNullValue())
        MatcherAssert.assertThat(ContractTestConfig.subKey, Matchers.notNullValue())

        world.configuration.apply {
            subscribeKey = ContractTestConfig.subKey
            publishKey = ContractTestConfig.pubKey
        }
    }

    @Given("the demo keyset with enabled storage")
    fun the_demo_keyset_with_enabled_storage() {
        the_demo_keyset()
    }
}
