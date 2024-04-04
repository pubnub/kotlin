package com.pubnub.api.endpoints.presence;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;

public interface GetState extends Endpoint<PNGetStateResult> {
    GetState channels(java.util.List<String> channels);

    GetState channelGroups(java.util.List<String> channelGroups);

    GetState uuid(String uuid);
}
