package com.pubnub.internal.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.objects_api.uuid.RemoveUUIDMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveUUIDMetadataImpl extends DelegatingEndpoint<PNRemoveUUIDMetadataResult> implements RemoveUUIDMetadata {

    private String uuid;

    public RemoveUUIDMetadataImpl(final CorePubNubClient pubnub) {
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
