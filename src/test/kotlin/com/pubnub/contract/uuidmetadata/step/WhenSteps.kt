package com.pubnub.contract.uuidmetadata.step

import com.pubnub.contract.state.World
import com.pubnub.contract.uuidmetadata.state.UUIDMetadataState
import io.cucumber.java.en.When

class WhenSteps(
    private val world: World,
    private val uuidMetadataState: UUIDMetadataState
) {

    @When("I get the UUID metadata")
    fun i_get_uuid_metadata() {
        uuidMetadataState.uuidMetadata = world.pubnub.getUUIDMetadata(uuid = uuidMetadataState.uuid).sync()?.data
    }

    @When("I get the UUID metadata with custom for current user")
    fun i_get_uuid_metadata_with_custom_for_current_user() {
        uuidMetadataState.uuidMetadata = world.pubnub.getUUIDMetadata(includeCustom = true).sync()?.data
    }

    @When("I set the UUID metadata")
    fun i_set_the_uuid_metadata() {
        val uuidMetadata = uuidMetadataState.uuidMetadata!!
        uuidMetadataState.uuidMetadata = null
        uuidMetadataState.uuidMetadata = world.pubnub.setUUIDMetadata(
            uuid = uuidMetadata.id,
            custom = uuidMetadata.custom,
            externalId = uuidMetadata.externalId,
            profileUrl = uuidMetadata.profileUrl,
            email = uuidMetadata.email,
            name = uuidMetadata.name,
            status = uuidMetadata.status,
            type = uuidMetadata.type
        ).sync()?.data
    }

    @When("I remove the UUID metadata")
    fun i_remove_uuid_metadata_for_id() {
        world.pubnub.removeUUIDMetadata(uuid = uuidMetadataState.uuid).sync()
    }

    @When("I remove the UUID metadata for current user")
    fun i_remove_uuid_metadata_of_current_user() {
        world.pubnub.removeUUIDMetadata().sync()
    }
}
