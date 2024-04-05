package com.pubnub.internal.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.objects_api.uuid.RemoveUUIDMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveUUIDMetadataImpl extends DelegatingEndpoint<PNRemoveMetadataResult, PNRemoveUUIDMetadataResult> implements RemoveUUIDMetadata {

    private String uuid;

    public RemoveUUIDMetadataImpl(final PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<PNRemoveMetadataResult> createAction() {
        return pubnub.removeUUIDMetadata(uuid);
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNRemoveUUIDMetadataResult> mapResult(@NotNull ExtendedRemoteAction<PNRemoveMetadataResult> action) {
        return new MappingRemoteAction<>(
                action,
                result -> new PNRemoveUUIDMetadataResult(
                        result.getStatus(),
                        null
                )
        );
    }
}
