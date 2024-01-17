package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveUUIDMetadata extends ValidatingEndpoint<PNRemoveUUIDMetadataResult> {

    private String uuid;

    public RemoveUUIDMetadata(final com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNRemoveUUIDMetadataResult> createAction() {
        return new MappingEndpoint<>(
                pubnub.removeUUIDMetadata(uuid),
                result -> new PNRemoveUUIDMetadataResult(
                        result.getStatus(),
                        null
                )
        );
    }
}
