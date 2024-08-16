package com.pubnub.api.java.endpoints.presence;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;

public interface GetState extends Endpoint<PNGetStateResult> {
    GetState channels(java.util.List<String> channels);

    GetState channelGroups(java.util.List<String> channelGroups);

    GetState uuid(String uuid);
}
