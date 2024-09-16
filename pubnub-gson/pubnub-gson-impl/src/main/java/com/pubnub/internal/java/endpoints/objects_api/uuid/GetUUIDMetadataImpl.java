package com.pubnub.internal.java.endpoints.objects_api.uuid;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.uuid.GetUUIDMetadata;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class GetUUIDMetadataImpl extends DelegatingEndpoint<PNUUIDMetadataResult, PNGetUUIDMetadataResult> implements GetUUIDMetadata {

    private String uuid;
    private boolean includeCustom;

    public GetUUIDMetadataImpl(final PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNUUIDMetadataResult> createRemoteAction() {
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
