package com.pubnub.contract.step

import com.pubnub.contract.state.World
import io.cucumber.java.en.Then
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

class ThenSteps(private val world: World) {

    @Then("an error is returned")
    fun an_error_is_returned() {
        assertNotNull(world.pnException)
    }

    @Then("the error status code is {int}")
    fun the_error_status_code_is(statusCode: Int) {
        assertEquals(statusCode, world.pnException?.statusCode)
    }

    @Then("the error message is {string}")
    fun the_error_message_is(message: String) {
        assertTrue("Exception ${world.pnException} should contain message $message", world.pnException?.message?.contains(message) ?: false)
    }

    @Then("the error source is {string}")
    fun the_error_source_is(source: String) {
        assertTrue("Exception ${world.pnException} should contain source $source", world.pnException?.message?.contains(source) ?: false)
    }

    @Then("the error detail message is {string}")
    fun the_error_detail_message_is(details: String) {
        assertTrue("Exception ${world.pnException} should contain error details $details", world.pnException?.message?.contains(details) ?: false)
    }

    @Then("the error detail location is {string}")
    fun the_error_detail_location_is(location: String) {
        assertTrue("Exception ${world.pnException} should contain location $location", world.pnException?.message?.contains(location) ?: false)
    }

    @Then("the error detail location type is {string}")
    fun the_error_detail_location_type_is(locationType: String) {
        assertTrue("Exception ${world.pnException} should contain locationType $locationType", world.pnException?.message?.contains(locationType) ?: false)
    }
}
