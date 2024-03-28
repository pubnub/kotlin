package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.channel.PNRemoveChannelMetadataResult;

public interface RemoveChannelMetadata extends Endpoint<PNRemoveChannelMetadataResult> {
    interface Builder extends BuilderSteps.ChannelStep<RemoveChannelMetadata> {
        @Override
        RemoveChannelMetadata channel(String channel);
    }
}
