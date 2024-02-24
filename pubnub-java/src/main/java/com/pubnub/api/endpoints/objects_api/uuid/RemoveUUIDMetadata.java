package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class RemoveUUIDMetadata extends DelegatingEndpoint<PNRemoveUUIDMetadataResult> {

    @Setter
    private String uuid;

    public RemoveUUIDMetadata(final InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected ExtendedRemoteAction<PNRemoveUUIDMetadataResult> createAction() {
        return new MappingRemoteAction<>(
                pubnub.removeUUIDMetadata(uuid),
                result -> new PNRemoveUUIDMetadataResult(
                        result.getStatus(),
                        null
                )
        );
    }
}
