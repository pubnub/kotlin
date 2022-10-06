package com.pubnub.contract.uuidmetadata.step

import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.contract.ContractTestConfig
import com.pubnub.contract.loadPersonaUUIDMetadata
import com.pubnub.contract.state.World
import com.pubnub.contract.uuidmetadata.state.UUIDMetadataState
import io.cucumber.java.en.Given
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

class GivenSteps(
    private val world: World,
    private val uuidMetadataState: UUIDMetadataState
) {
    @Given("I have a keyset with Objects V2 enabled")
    fun i_have_a_keyset_with_objects_v2_enabled() {
        MatcherAssert.assertThat(ContractTestConfig.subKey, Matchers.notNullValue())
        world.pubnub.configuration.apply {
            subscribeKey = ContractTestConfig.subKey
        }
    }

    @Given("the id for {string} persona")
    fun the_uuid_for_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersonaUUIDMetadata(personaName)
        uuidMetadataState.uuid = pnUUIDMetadata.id
    }

    @Given("current user is {string} persona")
    fun given_current_user_is_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersonaUUIDMetadata(personaName)
        world.pubnub.configuration.userId = UserId(pnUUIDMetadata.id)
    }

    @Given("the data for {string} persona")
    fun the_data_for_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersonaUUIDMetadata(personaName)
        uuidMetadataState.uuidMetadata = pnUUIDMetadata
    }
}
