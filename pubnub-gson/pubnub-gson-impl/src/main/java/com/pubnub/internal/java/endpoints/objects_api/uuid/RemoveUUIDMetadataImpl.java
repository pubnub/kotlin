package com.pubnub.internal.java.endpoints.objects_api.uuid;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.uuid.RemoveUUIDMetadata;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveUUIDMetadataImpl extends DelegatingEndpoint<PNRemoveMetadataResult, PNRemoveUUIDMetadataResult> implements RemoveUUIDMetadata {

    private String uuid;

    public RemoveUUIDMetadataImpl(final PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNRemoveMetadataResult> createAction() {
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
