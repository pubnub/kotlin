package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;

public interface GetUUIDMetadata extends Endpoint<PNGetUUIDMetadataResult> {
    GetUUIDMetadata uuid(String uuid);

    GetUUIDMetadata includeCustom(boolean includeCustom);
}
