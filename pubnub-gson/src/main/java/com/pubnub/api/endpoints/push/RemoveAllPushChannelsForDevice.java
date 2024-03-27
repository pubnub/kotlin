package com.pubnub.api.endpoints.push;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;

public interface RemoveAllPushChannelsForDevice extends Endpoint<PNPushRemoveAllChannelsResult> {
    RemoveAllPushChannelsForDevice pushType(com.pubnub.api.enums.PNPushType pushType);

    RemoveAllPushChannelsForDevice deviceId(String deviceId);

    RemoveAllPushChannelsForDevice environment(com.pubnub.api.enums.PNPushEnvironment environment);

    RemoveAllPushChannelsForDevice topic(String topic);
}
