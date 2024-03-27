package com.pubnub.api.endpoints.presence;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;

public interface SetState extends Endpoint<PNSetStateResult> {
    SetState channels(java.util.List<String> channels);

    SetState channelGroups(java.util.List<String> channelGroups);

    SetState state(Object state);

    SetState uuid(String uuid);

    SetState withHeartbeat(boolean withHeartbeat);
}
