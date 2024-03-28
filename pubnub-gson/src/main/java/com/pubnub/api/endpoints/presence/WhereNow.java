package com.pubnub.api.endpoints.presence;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult;

public interface WhereNow extends Endpoint<PNWhereNowResult> {
    WhereNow uuid(String uuid);
}
