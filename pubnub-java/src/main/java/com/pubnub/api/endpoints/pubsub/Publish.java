package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.internal.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class Publish extends Endpoint<PNPublishResult> {

    @Setter
    private Object message;
    @Setter
    private String channel;
    @Setter
    private Boolean shouldStore;
    @Setter
    private boolean usePOST;
    @Setter
    private Object meta;
    @Setter
    private boolean replicate;
    @Setter
    private Integer ttl;

    public Publish(PubNubImpl pubnub) {
        super(pubnub);
        this.replicate = true;
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNPublishResult> createAction() {
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
