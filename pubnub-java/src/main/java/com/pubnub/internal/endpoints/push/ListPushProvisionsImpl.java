package com.pubnub.internal.endpoints.push;

import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;
import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class ListPushProvisionsImpl extends DelegatingEndpoint<PNPushListProvisionsResult> implements com.pubnub.api.endpoints.push.ListPushProvisions {

    private PNPushType pushType;
    private String deviceId;
    private PNPushEnvironment environment;
    private String topic;

    public ListPushProvisionsImpl(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected ListPushProvisions createAction() {
        return pubnub.auditPushChannelProvisions(
                pushType,
                deviceId,
                topic,
                environment
        );
    }
}
