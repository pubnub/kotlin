package com.pubnub.internal.java.endpoints.pubsub;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.pubsub.Signal;
import com.pubnub.api.java.v2.endpoints.pubsub.SignalBuilder;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.internal.java.endpoints.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class SignalImpl extends IdentityMappingEndpoint<PNPublishResult> implements Signal, SignalBuilder {

    private Object message;
    private String channel;

    public SignalImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNPublishResult> createAction() {
        return pubnub.signal(channel, message);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (message == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_MESSAGE_MISSING);
        }
        if (channel == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}
