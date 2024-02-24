package com.pubnub.api.endpoints.access;

import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.InternalPubNubClient;
import kotlin.Unit;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class RevokeToken extends DelegatingEndpoint<Unit> {

    private String token;

    public RevokeToken(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, Unit> createAction() {
        return pubnub.revokeToken(token);
    }
}
