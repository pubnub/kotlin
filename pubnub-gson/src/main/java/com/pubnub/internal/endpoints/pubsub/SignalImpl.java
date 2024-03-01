package com.pubnub.internal.endpoints.pubsub;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.pubsub.Signal;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class SignalImpl extends DelegatingEndpoint<PNPublishResult> implements Signal {

    private Object message;
    private String channel;

    public SignalImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNPublishResult> createAction() {
        return pubnub.signal(channel, message);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (message == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_MESSAGE_MISSING);
        }
        if (channel == null || channel.isEmpty()) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}
