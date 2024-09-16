package com.pubnub.api.java.endpoints.objects_api.uuid;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNGetAllUUIDMetadataResult;

public interface GetAllUUIDMetadata extends Endpoint<PNGetAllUUIDMetadataResult> {
    GetAllUUIDMetadata includeCustom(boolean includeCustom);

    GetAllUUIDMetadata includeTotalCount(boolean includeTotalCount);

    GetAllUUIDMetadata limit(Integer limit);

    GetAllUUIDMetadata page(com.pubnub.api.models.consumer.objects.PNPage page);

    GetAllUUIDMetadata filter(String filter);

    GetAllUUIDMetadata sort(java.util.Collection<PNSortKey> sort);
}
