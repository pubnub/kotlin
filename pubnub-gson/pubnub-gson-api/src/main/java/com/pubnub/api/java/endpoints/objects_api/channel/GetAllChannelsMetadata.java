package com.pubnub.api.java.endpoints.objects_api.channel;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult;

import java.util.Collection;

public interface GetAllChannelsMetadata extends Endpoint<PNGetAllChannelsMetadataResult> {
    GetAllChannelsMetadata limit(Integer limit);

    GetAllChannelsMetadata page(com.pubnub.api.models.consumer.objects.PNPage page);

    GetAllChannelsMetadata filter(String filter);

    GetAllChannelsMetadata sort(Collection<PNSortKey> sort);

    GetAllChannelsMetadata includeTotalCount(boolean includeTotalCount);

    GetAllChannelsMetadata includeCustom(boolean includeCustom);
}
