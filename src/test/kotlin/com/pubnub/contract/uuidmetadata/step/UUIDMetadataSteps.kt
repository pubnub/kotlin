package com.pubnub.contract.uuidmetadata.step

import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.contract.loadPersonaUUIDMetadata
import com.pubnub.contract.uuidmetadata.state.UUIDMetadataState
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.Is

class UUIDMetadataSteps(
    private val uuidMetadataState: UUIDMetadataState
) {

    @Given("the id for {string} persona")
    fun the_uuid_for_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersonaUUIDMetadata(personaName)
        uuidMetadataState.uuid = pnUUIDMetadata.id
    }

    @Given("current user is {string} persona")
    fun given_current_user_is_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersonaUUIDMetadata(personaName)
        uuidMetadataState.pubnub.configuration.userId = UserId(pnUUIDMetadata.id)
    }

    @Given("the data for {string} persona")
    fun the_data_for_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersonaUUIDMetadata(personaName)
        uuidMetadataState.uuidMetadata = pnUUIDMetadata
    }

    @When("I get the UUID metadata")
    fun i_get_uuid_metadata() {
        uuidMetadataState.pubnub.getUUIDMetadata(uuid = uuidMetadataState.uuid).sync()?.let {
            uuidMetadataState.uuidMetadata = it.data
            uuidMetadataState.responseStatus = it.status
        }
    }

    @When("I get the UUID metadata with custom for current user")
    fun i_get_uuid_metadata_with_custom_for_current_user() {
        uuidMetadataState.pubnub.getUUIDMetadata(includeCustom = true).sync()?.let {
            uuidMetadataState.uuidMetadata = it.data
            uuidMetadataState.responseStatus = it.status
        }
    }

    @When("I set the UUID metadata")
    fun i_set_the_uuid_metadata() {
        val uuidMetadata = uuidMetadataState.uuidMetadata!!
        uuidMetadataState.pubnub.setUUIDMetadata(
            uuid = uuidMetadata.id,
            custom = uuidMetadata.custom,
            externalId = uuidMetadata.externalId,
            profileUrl = uuidMetadata.profileUrl,
            email = uuidMetadata.email,
            name = uuidMetadata.name,
            status = uuidMetadata.status,
            type = uuidMetadata.type
        ).sync()?.let {
            uuidMetadataState.uuidMetadata = it.data
            uuidMetadataState.responseStatus = it.status
        }
    }

    @When("I remove the UUID metadata")
    fun i_remove_uuid_metadata_for_id() {
        uuidMetadataState.responseStatus = uuidMetadataState.pubnub.removeUUIDMetadata(uuid = uuidMetadataState.uuid).sync()?.status
    }

    @When("I remove the UUID metadata for current user")
    fun i_remove_uuid_metadata_of_current_user() {
        uuidMetadataState.responseStatus = uuidMetadataState.pubnub.removeUUIDMetadata().sync()?.status
    }

    @When("I get all UUID metadata")
    fun i_get_all_uuid_metadata() {
        uuidMetadataState.pubnub.getAllUUIDMetadata().sync()?.let {
            uuidMetadataState.uuidMetadatas = it.data
            uuidMetadataState.responseStatus = it.status
        }
    }

    @When("I get all UUID metadata with custom")
    fun i_get_all_uuid_metadata_with_custom() {
        uuidMetadataState.pubnub.getAllUUIDMetadata(includeCustom = true).sync()?.let {
            uuidMetadataState.uuidMetadatas = it.data
            uuidMetadataState.responseStatus = it.status
        }
    }

    @Then("the UUID metadata for {string} persona")
    fun the_uuid_metadata_for_persona(personaName: String) {
        val alice: PNUUIDMetadata = loadPersonaUUIDMetadata(personaName)
        MatcherAssert.assertThat(uuidMetadataState.uuidMetadata, Is.`is`(alice))
    }

    @Then("the UUID metadata for {string} persona contains updated")
    fun the_uuid_metadata_for_persona_contains_updated(@Suppress("UNUSED_PARAMETER") personaName: String) {
        MatcherAssert.assertThat(uuidMetadataState.uuidMetadata?.updated, Is.`is`(Matchers.notNullValue()))
    }

    @Then("the response contains list with {string} and {string} UUID metadata")
    fun the_response_contains_list_with_UUID_metadata(personName: String, otherPersonName: String) {
        val firstPersona = loadPersonaUUIDMetadata(personName)
        val secondPersona = loadPersonaUUIDMetadata(otherPersonName)
        MatcherAssert.assertThat(
            uuidMetadataState.uuidMetadatas,
            Matchers.containsInAnyOrder(firstPersona, secondPersona)
        )
    }
}
