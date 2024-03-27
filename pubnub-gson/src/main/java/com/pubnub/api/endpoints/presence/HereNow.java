package com.pubnub.api.endpoints.presence;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;

public interface HereNow extends Endpoint<PNHereNowResult> {
    HereNow channels(java.util.List<String> channels);

    HereNow channelGroups(java.util.List<String> channelGroups);

    HereNow includeState(boolean includeState);

    HereNow includeUUIDs(boolean includeUUIDs);
}
