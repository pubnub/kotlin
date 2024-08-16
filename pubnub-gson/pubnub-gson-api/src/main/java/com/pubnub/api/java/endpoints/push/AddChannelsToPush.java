package com.pubnub.api.java.endpoints.push;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;

public interface AddChannelsToPush extends Endpoint<PNPushAddChannelResult> {
    AddChannelsToPush pushType(com.pubnub.api.enums.PNPushType pushType);

    AddChannelsToPush channels(java.util.List<String> channels);

    AddChannelsToPush deviceId(String deviceId);

    AddChannelsToPush environment(com.pubnub.api.enums.PNPushEnvironment environment);

    AddChannelsToPush topic(String topic);
}
