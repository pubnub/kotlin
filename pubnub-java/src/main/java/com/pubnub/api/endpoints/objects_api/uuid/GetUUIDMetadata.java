package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.internal.InternalPubNubClient;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class GetUUIDMetadata extends Endpoint<PNGetUUIDMetadataResult> {

    private String uuid;
    private boolean includeCustom;

    public GetUUIDMetadata(final InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected ExtendedRemoteAction<PNGetUUIDMetadataResult> createAction() {
        return new MappingRemoteAction<>(pubnub.getUUIDMetadata(
                uuid,
                includeCustom
        ), result -> new PNGetUUIDMetadataResult(
                result.getStatus(),
                PNUUIDMetadata.from(result.getData())
        ));
    }
}
