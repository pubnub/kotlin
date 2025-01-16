package com.pubnub.api.java.endpoints.objects_api.uuid;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface SetUUIDMetadata extends Endpoint<PNSetUUIDMetadataResult> {
    SetUUIDMetadata custom(Map<String, Object> custom);

    SetUUIDMetadata uuid(String uuid);

    SetUUIDMetadata name(String name);

    SetUUIDMetadata externalId(String externalId);

    SetUUIDMetadata profileUrl(String profileUrl);

    SetUUIDMetadata email(String email);

    SetUUIDMetadata includeCustom(boolean includeCustom);

    SetUUIDMetadata type(String type);

    SetUUIDMetadata status(String status);

    /**
     * Optional entity tag from a previously received `PNUUIDMetadata`. The request
     * will fail if this parameter is specified and the ETag value on the server doesn't match.
     * @param etag from PNUUIDMetadata
     * @return this builder
     */
    SetUUIDMetadata ifMatchesEtag(@Nullable String etag);
}
