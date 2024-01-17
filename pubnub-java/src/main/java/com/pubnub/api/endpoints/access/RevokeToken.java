package com.pubnub.api.endpoints.access;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import kotlin.Unit;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class RevokeToken extends ValidatingEndpoint<Unit> {

    private String token;

    public RevokeToken(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<Unit> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.revokeToken(token));
    }
}
