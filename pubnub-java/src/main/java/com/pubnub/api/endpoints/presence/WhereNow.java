package com.pubnub.api.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class WhereNow extends ValidatingEndpoint<PNWhereNowResult> {

    @Setter
    private String uuid;

    public WhereNow(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    protected Endpoint<PNWhereNowResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.whereNow(uuid));
    }
}
