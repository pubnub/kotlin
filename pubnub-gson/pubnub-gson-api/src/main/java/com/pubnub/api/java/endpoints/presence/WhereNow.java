package com.pubnub.api.java.endpoints.presence;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.presence.PNWhereNowResult;

public interface WhereNow extends Endpoint<PNWhereNowResult> {
    WhereNow uuid(String uuid);
}
