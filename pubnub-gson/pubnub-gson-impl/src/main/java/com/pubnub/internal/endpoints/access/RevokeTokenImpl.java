package com.pubnub.internal.endpoints.access;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.access.RevokeToken;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.IdentityMappingEndpoint;
import kotlin.Unit;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class RevokeTokenImpl extends IdentityMappingEndpoint<Unit> implements RevokeToken {

    private String token;

    public RevokeTokenImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<Unit> createAction() {
        return pubnub.revokeToken(token);
    }


    @Override
    protected void validateParams() throws PubNubException {
        if (this.token == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_TOKEN_MISSING);
        }
    }
}
