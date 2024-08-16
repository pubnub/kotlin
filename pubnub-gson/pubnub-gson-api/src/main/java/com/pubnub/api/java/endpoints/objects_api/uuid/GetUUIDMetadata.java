package com.pubnub.api.java.endpoints.objects_api.uuid;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;

public interface GetUUIDMetadata extends Endpoint<PNGetUUIDMetadataResult> {
    GetUUIDMetadata uuid(String uuid);

    GetUUIDMetadata includeCustom(boolean includeCustom);
}
