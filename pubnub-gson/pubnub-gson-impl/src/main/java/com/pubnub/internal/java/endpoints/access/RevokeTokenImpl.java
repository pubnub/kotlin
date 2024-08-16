package com.pubnub.internal.java.endpoints.access;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.access.RevokeToken;
import com.pubnub.internal.java.endpoints.IdentityMappingEndpoint;
import kotlin.Unit;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class RevokeTokenImpl extends IdentityMappingEndpoint<Unit> implements RevokeToken {

    private String token;

    public RevokeTokenImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<Unit> createAction() {
        return pubnub.revokeToken(token);
    }


    @Override
    protected void validateParams() throws PubNubException {
        if (this.token == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_TOKEN_MISSING);
        }
    }
}
