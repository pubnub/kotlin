package com.pubnub.api.endpoints.access;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import kotlin.Unit;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class RevokeToken extends Endpoint<Unit> {

    @Setter
    private String token;

    public RevokeToken(PubNubImpl pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, Unit> createAction() {
        return pubnub.revokeToken(token);
    }
}
