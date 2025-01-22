package com.pubnub.api.java.endpoints.objects_api.channel;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface SetChannelMetadata extends Endpoint<PNSetChannelMetadataResult> {
    SetChannelMetadata custom(Map<String, Object> custom);

    SetChannelMetadata description(String description);

    SetChannelMetadata name(String name);

    SetChannelMetadata status(String status);

    SetChannelMetadata type(String type);

    SetChannelMetadata includeCustom(boolean includeCustom);

    /**
     * Optional entity tag from a previously received `PNChannelMetadata`. The request
     * will fail if this parameter is specified and the ETag value on the server doesn't match.
     * @param etag from PNChannelMetadata
     * @return this builder
     */
    SetChannelMetadata ifMatchesEtag(@Nullable String etag);

    interface Builder extends BuilderSteps.ChannelStep<SetChannelMetadata> {
        @Override
        SetChannelMetadata channel(String channel);
    }
}
