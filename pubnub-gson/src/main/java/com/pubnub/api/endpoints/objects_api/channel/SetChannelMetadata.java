package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.channel.PNSetChannelMetadataResult;

import java.util.Map;

public interface SetChannelMetadata extends Endpoint<PNSetChannelMetadataResult> {
    SetChannelMetadata custom(Map<String, Object> custom);

    SetChannelMetadata description(String description);

    SetChannelMetadata name(String name);

    SetChannelMetadata status(String status);

    SetChannelMetadata type(String type);

    SetChannelMetadata includeCustom(boolean includeCustom);

    interface Builder extends BuilderSteps.ChannelStep<SetChannelMetadata> {
        @Override
        SetChannelMetadata channel(String channel);
    }
}
