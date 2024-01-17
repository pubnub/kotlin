package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.PNPublishResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class Publish extends ValidatingEndpoint<PNPublishResult> {

    private Object message;
    private String channel;
    private Boolean shouldStore;
    private boolean usePOST;
    private Object meta;
    private boolean replicate;
    private Integer ttl;

    public Publish(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        this.replicate = true;
    }

    @Override
    protected Endpoint<PNPublishResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.publish(
                channel,
                message,
                meta,
                shouldStore,
                usePOST,
                replicate,
                ttl
        ));
    }
}
