package com.pubnub.api.endpoints.objects_api.uuid

import com.pubnub.api.endpoints.Endpoint
import com.pubnub.api.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult
import com.pubnub.api.utils.Optional

interface UpdateUserMetadata : Endpoint<PNSetUUIDMetadataResult?> {
    fun uuid(uuid: String): UpdateUserMetadata

    fun name(name: Optional<String?>): UpdateUserMetadata

    fun externalId(externalId: Optional<String?>): UpdateUserMetadata

    fun profileUrl(profileUrl: Optional<String?>): UpdateUserMetadata

    fun email(email: Optional<String?>): UpdateUserMetadata

    fun custom(custom: Optional<Map<String, Any?>?>): UpdateUserMetadata

    fun includeCustom(includeCustom: Boolean): UpdateUserMetadata

    fun type(type: Optional<String?>): UpdateUserMetadata

    fun status(status: Optional<String?>): UpdateUserMetadata
}
