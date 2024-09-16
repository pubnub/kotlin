package com.pubnub.api.java.endpoints.objects_api.uuid;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult;

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
}
