package com.pubnub.internal.endpoints.pubsub;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.v2.endpoints.pubsub.PublishBuilder;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class PublishImpl extends DelegatingEndpoint<PNPublishResult> implements Publish, PublishBuilder {

    private Object message;
    private String channel;
    private Boolean shouldStore;
    private boolean usePOST;
    private Object meta;
    private boolean replicate;
    private Integer ttl;

    public PublishImpl(PubNubCore pubnub) {
        super(pubnub);
        this.replicate = true;
    }

    @Override
    protected ExtendedRemoteAction<PNPublishResult> createAction() {
        return pubnub.publish(
                channel,
                message,
                meta,
                shouldStore,
                usePOST,
                replicate,
                ttl
        );
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
