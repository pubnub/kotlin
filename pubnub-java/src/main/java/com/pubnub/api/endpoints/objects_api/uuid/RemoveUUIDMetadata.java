package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class RemoveUUIDMetadata extends Endpoint<PNRemoveUUIDMetadataResult> {

    @Setter
    private String uuid;

    public RemoveUUIDMetadata(final com.pubnub.internal.PubNub pubnub) {
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
