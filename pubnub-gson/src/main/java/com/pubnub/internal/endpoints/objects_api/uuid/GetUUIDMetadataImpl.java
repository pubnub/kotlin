package com.pubnub.internal.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.objects_api.uuid.GetUUIDMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class GetUUIDMetadataImpl extends DelegatingEndpoint<PNGetUUIDMetadataResult> implements GetUUIDMetadata {

    private String uuid;
    private boolean includeCustom;

    public GetUUIDMetadataImpl(final PubNubCore pubnub) {
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
