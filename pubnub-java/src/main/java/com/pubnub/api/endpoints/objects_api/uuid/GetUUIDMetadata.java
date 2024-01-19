package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class GetUUIDMetadata extends ValidatingEndpoint<PNGetUUIDMetadataResult> {

    private String uuid;
    private boolean includeCustom;

    public GetUUIDMetadata(final com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNGetUUIDMetadataResult> createAction() {
        return new MappingEndpoint<>(pubnub.getUUIDMetadata(
                uuid,
                includeCustom
        ), result -> new PNGetUUIDMetadataResult(
                result.getStatus(),
                PNUUIDMetadata.from(result.getData())
        ));
    }
}
