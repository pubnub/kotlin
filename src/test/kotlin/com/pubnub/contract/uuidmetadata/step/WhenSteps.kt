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
        world.pubnub.getUUIDMetadata(uuid = uuidMetadataState.uuid).sync()?.let {
            uuidMetadataState.uuidMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get the UUID metadata with custom for current user")
    fun i_get_uuid_metadata_with_custom_for_current_user() {
        world.pubnub.getUUIDMetadata(includeCustom = true).sync()?.let {
            uuidMetadataState.uuidMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I set the UUID metadata")
    fun i_set_the_uuid_metadata() {
        val uuidMetadata = uuidMetadataState.uuidMetadata!!
        world.pubnub.setUUIDMetadata(
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
            world.responseStatus = it.status
        }
    }

    @When("I remove the UUID metadata")
    fun i_remove_uuid_metadata_for_id() {
        world.responseStatus = world.pubnub.removeUUIDMetadata(uuid = uuidMetadataState.uuid).sync()?.status
    }

    @When("I remove the UUID metadata for current user")
    fun i_remove_uuid_metadata_of_current_user() {
        world.responseStatus = world.pubnub.removeUUIDMetadata().sync()?.status
    }

    @When("I get all UUID metadata")
    fun i_get_all_uuid_metadata() {
        world.pubnub.getAllUUIDMetadata().sync()?.let {
            uuidMetadataState.uuidMetadatas = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get all UUID metadata with custom")
    fun i_get_all_uuid_metadata_with_custom() {
        world.pubnub.getAllUUIDMetadata(includeCustom = true).sync()?.let {
            uuidMetadataState.uuidMetadatas = it.data
            world.responseStatus = it.status
        }
    }
}
