package com.pubnub.contract.uuidmetadata.step

import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.contract.loadPersona
import com.pubnub.contract.state.World
import com.pubnub.contract.uuidmetadata.state.UUIDMetadataState
import io.cucumber.java.en.Given

class GivenSteps(
    private val world: World,
    private val uuidMetadataState: UUIDMetadataState
) {
    @Given("I have a keyset with Objects V2 enabled")
    fun i_have_a_keyset_with_objects_v2_enabled() {
        world.pubnub.configuration.apply {
            subscribeKey = "demo"
            publishKey = "demo"
        }
    }

    @Given("the id for {string} persona")
    fun the_uuid_for_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersona(personaName)
        uuidMetadataState.uuid = pnUUIDMetadata.id
    }

    @Given("current user is {string} persona")
    fun given_current_user_is_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersona(personaName)
        world.pubnub.configuration.userId = UserId(pnUUIDMetadata.id)
    }

    @Given("the data for {string} persona")
    fun the_data_for_persona(personaName: String) {
        val pnUUIDMetadata: PNUUIDMetadata = loadPersona(personaName)
        uuidMetadataState.uuidMetadata = pnUUIDMetadata
    }
}
