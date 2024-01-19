package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetAllUUIDMetadataResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Collections;

import static com.pubnub.api.endpoints.objects_api.channel.GetAllChannelsMetadata.toInternal;

@Setter
@Accessors(chain = true, fluent = true)
public class GetAllUUIDMetadata
        extends ValidatingEndpoint<PNGetAllUUIDMetadataResult> {

    private boolean includeCustom;
    private boolean includeTotalCount;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();

    public GetAllUUIDMetadata(final com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNGetAllUUIDMetadataResult> createAction() {
        return new MappingEndpoint<>(
                pubnub.getAllUUIDMetadata(
                        limit,
                        page,
                        filter,
                        toInternal(sort),
                        includeTotalCount,
                        includeCustom
                ),
                PNGetAllUUIDMetadataResult::from);
    }
}