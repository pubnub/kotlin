package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;

public interface RemoveUUIDMetadata extends Endpoint<PNRemoveUUIDMetadataResult> {
    RemoveUUIDMetadata uuid(String uuid);
}
