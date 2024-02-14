package com.pubnub.api.endpoints.push;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;
import com.pubnub.internal.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class ListPushProvisions extends Endpoint<PNPushListProvisionsResult> {

    @Setter
    private PNPushType pushType;
    @Setter
    private String deviceId;
    @Setter
    private PNPushEnvironment environment;
    @Setter
    private String topic;

    public ListPushProvisions(PubNubImpl pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.endpoints.push.ListPushProvisions createAction() {
        return pubnub.auditPushChannelProvisions(
                pushType,
                deviceId,
                topic,
                environment
        );
    }
}
