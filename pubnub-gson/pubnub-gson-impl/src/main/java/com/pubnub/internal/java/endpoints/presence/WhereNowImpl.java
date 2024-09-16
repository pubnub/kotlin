package com.pubnub.internal.java.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.java.endpoints.presence.WhereNow;
import com.pubnub.api.models.consumer.presence.PNWhereNowResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class WhereNowImpl extends PassthroughEndpoint<PNWhereNowResult> implements WhereNow {

    private String uuid;

    public WhereNowImpl(PubNub pubnub) {
        super(pubnub);
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    @NotNull
    protected Endpoint<PNWhereNowResult> createRemoteAction() {
        return pubnub.whereNow(uuid);
    }
}
