package com.pubnub.api.java.endpoints.objects_api.uuid;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;

public interface RemoveUUIDMetadata extends Endpoint<PNRemoveUUIDMetadataResult> {
    RemoveUUIDMetadata uuid(String uuid);
}
