package com.pubnub.api.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.presence.PNWhereNowResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class WhereNow extends ValidatingEndpoint<PNWhereNowResult> {

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
