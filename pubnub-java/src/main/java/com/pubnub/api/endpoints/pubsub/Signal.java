package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class Signal extends Endpoint<PNPublishResult> {

    @Setter
    private Object message;

    @Setter
    private String channel;

    public Signal(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNPublishResult> createAction() {
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
