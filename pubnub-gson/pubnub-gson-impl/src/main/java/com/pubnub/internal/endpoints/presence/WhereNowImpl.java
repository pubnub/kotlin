package com.pubnub.internal.endpoints.presence;

import com.pubnub.api.endpoints.presence.WhereNow;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.IdentityMappingEndpoint;
import com.pubnub.internal.models.consumer.presence.PNWhereNowResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class WhereNowImpl extends IdentityMappingEndpoint<PNWhereNowResult> implements WhereNow {

    private String uuid;

    public WhereNowImpl(PubNubCore pubnub) {
        super(pubnub);
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    @NotNull
    protected EndpointInterface<PNWhereNowResult> createAction() {
        return pubnub.whereNow(uuid);
    }
}
