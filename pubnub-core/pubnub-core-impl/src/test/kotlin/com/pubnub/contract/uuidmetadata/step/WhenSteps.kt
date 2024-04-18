package com.pubnub.contract.uuidmetadata.step

import com.pubnub.api.utils.Optional
import com.pubnub.contract.state.World
import com.pubnub.contract.uuidmetadata.state.UUIDMetadataState
import io.cucumber.java.en.When

class WhenSteps(
    private val world: World,
    private val uuidMetadataState: UUIDMetadataState,
) {
    @When("I get the UUID metadata")
    fun i_get_uuid_metadata() {
        world.pubnub.pubNubCore.getUUIDMetadata(uuid = uuidMetadataState.uuid).sync()?.let {
            uuidMetadataState.uuidMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get the UUID metadata with custom for current user")
    fun i_get_uuid_metadata_with_custom_for_current_user() {
        world.pubnub.pubNubCore.getUUIDMetadata(includeCustom = true).sync()?.let {
            uuidMetadataState.uuidMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I set the UUID metadata")
    fun i_set_the_uuid_metadata() {
        val uuidMetadata = uuidMetadataState.uuidMetadata!!
        world.pubnub.pubNubCore.setUUIDMetadata(
            uuid = uuidMetadata.id,
            custom = Optional.of(uuidMetadata.custom),
            externalId = Optional.of(uuidMetadata.externalId),
            profileUrl = Optional.of(uuidMetadata.profileUrl),
            email = Optional.of(uuidMetadata.email),
            name = Optional.of(uuidMetadata.name),
            status = Optional.of(uuidMetadata.status),
            type = Optional.of(uuidMetadata.type),
        ).sync().let {
            uuidMetadataState.uuidMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I remove the UUID metadata")
    fun i_remove_uuid_metadata_for_id() {
        world.responseStatus = world.pubnub.pubNubCore.removeUUIDMetadata(uuid = uuidMetadataState.uuid).sync()?.status
    }

    @When("I remove the UUID metadata for current user")
    fun i_remove_uuid_metadata_of_current_user() {
        world.responseStatus = world.pubnub.pubNubCore.removeUUIDMetadata().sync()?.status
    }

    @When("I get all UUID metadata")
    fun i_get_all_uuid_metadata() {
        world.pubnub.pubNubCore.getAllUUIDMetadata().sync()?.let {
            uuidMetadataState.uuidMetadatas = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get all UUID metadata with custom")
    fun i_get_all_uuid_metadata_with_custom() {
        world.pubnub.pubNubCore.getAllUUIDMetadata(includeCustom = true).sync()?.let {
            uuidMetadataState.uuidMetadatas = it.data
            world.responseStatus = it.status
        }
    }
}
