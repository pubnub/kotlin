package com.pubnub.internal.endpoints.pubsub;

import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class PublishImpl extends DelegatingEndpoint<PNPublishResult> implements Publish {

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
    protected com.pubnub.internal.EndpointCore<?, PNPublishResult> createAction() {
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
}
