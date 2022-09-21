package com.pubnub.contract.uuidmetadata.step

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.contract.loadPersona
import com.pubnub.contract.state.World
import com.pubnub.contract.uuidmetadata.state.UUIDMetadataState
import io.cucumber.java.en.Then
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.core.Is.`is` as iz

class ThenSteps(
    private val world: World,
    private val uuidMetadataState: UUIDMetadataState
) {

    @Then("I receive a successful response")
    fun remove_uuid_metadata_is_success() {
        assertThat(world.pnException, Matchers.nullValue())
    }

    @Then("the UUID metadata for {string} persona")
    fun the_uuid_metadata_for_alice_persona(personaName: String) {
        val alice: PNUUIDMetadata = loadPersona(personaName)
        assertThat(uuidMetadataState.uuidMetadata, iz(alice))
    }

    @Then("the UUID metadata for 'Alice' persona contains updated")
    fun the_uuid_metadata_for_persona_contains_updated() {
        assertThat(uuidMetadataState.uuidMetadata?.updated, iz(Matchers.notNullValue()))
    }

    @Then("the UUID metadata for {string} and {string} persona")
    fun the_uuid_metadata_for_and_persona(personName: String, otherPersonName: String) {
    }
}
