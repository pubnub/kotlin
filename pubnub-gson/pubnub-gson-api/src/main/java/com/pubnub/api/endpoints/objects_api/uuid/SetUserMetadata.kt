package com.pubnub.api.endpoints.objects_api.uuid

import com.pubnub.api.endpoints.Endpoint
import com.pubnub.api.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult
import com.pubnub.api.utils.Optional

interface SetUserMetadata : Endpoint<PNSetUUIDMetadataResult?> {
    fun uuid(uuid: String): SetUserMetadata

    fun name(name: Optional<String?>): SetUserMetadata

    fun externalId(externalId: Optional<String?>): SetUserMetadata

    fun profileUrl(profileUrl: Optional<String?>): SetUserMetadata

    fun email(email: Optional<String?>): SetUserMetadata

    fun custom(custom: Optional<Map<String, Any?>?>): SetUserMetadata

    fun includeCustom(includeCustom: Boolean): SetUserMetadata

    fun type(type: Optional<String?>): SetUserMetadata

    fun status(status: Optional<String?>): SetUserMetadata
}
