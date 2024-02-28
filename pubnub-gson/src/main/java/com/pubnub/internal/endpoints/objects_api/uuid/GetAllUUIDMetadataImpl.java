package com.pubnub.internal.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.objects_api.uuid.GetAllUUIDMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetAllUUIDMetadataResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Collections;

import static com.pubnub.internal.endpoints.objects_api.channel.GetAllChannelsMetadataImpl.toInternal;

@Setter
@Accessors(chain = true, fluent = true)
public class GetAllUUIDMetadataImpl
        extends DelegatingEndpoint<PNGetAllUUIDMetadataResult> implements GetAllUUIDMetadata {

    private boolean includeCustom;
    private boolean includeTotalCount;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();

    public GetAllUUIDMetadataImpl(final CorePubNubClient pubnub) {
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