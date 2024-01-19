package com.pubnub.api.endpoints.push;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class ListPushProvisions extends ValidatingEndpoint<PNPushListProvisionsResult> {

    private PNPushType pushType;
    private String deviceId;
    private PNPushEnvironment environment;
    private String topic;

    public ListPushProvisions(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNPushListProvisionsResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.auditPushChannelProvisions(
                pushType,
                deviceId,
                topic,
                environment
        ));
    }
}
