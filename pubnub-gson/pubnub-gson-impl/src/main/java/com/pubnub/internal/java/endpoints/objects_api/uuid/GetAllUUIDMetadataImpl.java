package com.pubnub.internal.java.endpoints.objects_api.uuid;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.endpoints.objects_api.uuid.GetAllUUIDMetadata;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNGetAllUUIDMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNGetAllUUIDMetadataResultConverter;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

import static com.pubnub.internal.java.endpoints.objects_api.channel.GetAllChannelsMetadataImpl.toInternal;

@Setter
@Accessors(chain = true, fluent = true)
public class GetAllUUIDMetadataImpl
        extends DelegatingEndpoint<PNUUIDMetadataArrayResult, PNGetAllUUIDMetadataResult> implements GetAllUUIDMetadata {

    private boolean includeCustom;
    private boolean includeTotalCount;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();

    public GetAllUUIDMetadataImpl(final PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNUUIDMetadataArrayResult> createRemoteAction() {
        return pubnub.getAllUUIDMetadata(
                limit,
                page,
                filter,
                toInternal(sort),
                includeTotalCount,
                includeCustom
        );
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetAllUUIDMetadataResult> mapResult(@NotNull ExtendedRemoteAction<PNUUIDMetadataArrayResult> action) {
        return new MappingRemoteAction<>(action, PNGetAllUUIDMetadataResultConverter::from);
    }
}