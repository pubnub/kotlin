package com.pubnub.api.java.endpoints.objects_api.channel;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNRemoveChannelMetadataResult;

public interface RemoveChannelMetadata extends Endpoint<PNRemoveChannelMetadataResult> {
    interface Builder extends BuilderSteps.ChannelStep<RemoveChannelMetadata> {
        @Override
        RemoveChannelMetadata channel(String channel);
    }
}
