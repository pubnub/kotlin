package com.pubnub.api.endpoints.push;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;

public interface RemoveChannelsFromPush extends Endpoint<PNPushRemoveChannelResult> {
    RemoveChannelsFromPush pushType(com.pubnub.api.enums.PNPushType pushType);

    RemoveChannelsFromPush channels(java.util.List<String> channels);

    RemoveChannelsFromPush deviceId(String deviceId);

    RemoveChannelsFromPush environment(com.pubnub.api.enums.PNPushEnvironment environment);

    RemoveChannelsFromPush topic(String topic);
}
