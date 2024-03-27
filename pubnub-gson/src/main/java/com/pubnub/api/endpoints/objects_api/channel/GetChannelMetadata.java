package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetChannelMetadataResult;

public interface GetChannelMetadata extends Endpoint<PNGetChannelMetadataResult> {
    GetChannelMetadata includeCustom(boolean includeCustom);

    interface Builder extends BuilderSteps.ChannelStep<GetChannelMetadata> {
        @Override
        GetChannelMetadata channel(String channel);
    }
}
