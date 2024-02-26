package com.pubnub.internal.endpoints.presence;

import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class WhereNowImpl extends DelegatingEndpoint<PNWhereNowResult> implements com.pubnub.api.endpoints.presence.WhereNow {

    private String uuid;

    public WhereNowImpl(InternalPubNubClient pubnub) {
        super(pubnub);
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    protected WhereNow createAction() {
        return pubnub.whereNow(uuid);
    }
}
