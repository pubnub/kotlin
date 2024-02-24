package com.pubnub.api.endpoints.presence;

import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class WhereNow extends DelegatingEndpoint<PNWhereNowResult> {

    @Setter
    private String uuid;

    public WhereNow(InternalPubNubClient pubnub) {
        super(pubnub);
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    protected com.pubnub.internal.endpoints.presence.WhereNow createAction() {
        return pubnub.whereNow(uuid);
    }
}
