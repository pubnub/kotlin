package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetAllUUIDMetadataResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Collections;

import static com.pubnub.api.endpoints.objects_api.channel.GetAllChannelsMetadata.toInternal;

@Accessors(chain = true, fluent = true)
public class GetAllUUIDMetadata
        extends Endpoint<PNGetAllUUIDMetadataResult> {

    @Setter
    private boolean includeCustom;
    @Setter
    private boolean includeTotalCount;
    @Setter
    private Integer limit;
    @Setter
    private PNPage page;
    @Setter
    private String filter;
    @Setter
    private Collection<PNSortKey> sort = Collections.emptyList();

    public GetAllUUIDMetadata(final com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected ExtendedRemoteAction<PNGetAllUUIDMetadataResult> createAction() {
        return new MappingRemoteAction<>(
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