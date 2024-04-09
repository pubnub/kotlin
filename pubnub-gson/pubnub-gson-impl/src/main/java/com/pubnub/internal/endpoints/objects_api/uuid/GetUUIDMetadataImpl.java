package com.pubnub.internal.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.objects_api.uuid.GetUUIDMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class GetUUIDMetadataImpl extends DelegatingEndpoint<PNUUIDMetadataResult, PNGetUUIDMetadataResult> implements GetUUIDMetadata {

    private String uuid;
    private boolean includeCustom;

    public GetUUIDMetadataImpl(final PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<PNUUIDMetadataResult> createAction() {
        return pubnub.getUUIDMetadata(
                uuid,
                includeCustom
        );
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetUUIDMetadataResult> mapResult(@NotNull ExtendedRemoteAction<PNUUIDMetadataResult> action) {
        return new MappingRemoteAction<>(action, result -> new PNGetUUIDMetadataResult(
                result.getStatus(),
                PNUUIDMetadata.from(result.getData())
        ));
    }
}
