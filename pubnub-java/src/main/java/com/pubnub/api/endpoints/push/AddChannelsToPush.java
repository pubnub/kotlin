package com.pubnub.api.endpoints.push;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class AddChannelsToPush extends ValidatingEndpoint<PNPushAddChannelResult> {

    private PNPushType pushType;
    private List<String> channels;
    private String deviceId;
    private PNPushEnvironment environment = PNPushEnvironment.DEVELOPMENT;
    private String topic;

    public AddChannelsToPush(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNPushAddChannelResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.addPushNotificationsOnChannels(
                pushType,
                channels,
                deviceId,
                topic,
                environment
        ));
    }
}
