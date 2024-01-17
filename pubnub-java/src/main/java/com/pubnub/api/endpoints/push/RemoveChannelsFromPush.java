package com.pubnub.api.endpoints.push;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true, fluent = true)
public class RemoveChannelsFromPush extends ValidatingEndpoint<PNPushRemoveChannelResult> {

    @Setter
    private PNPushType pushType;
    @Setter
    private List<String> channels;
    @Setter
    private String deviceId;
    @Setter
    private PNPushEnvironment environment;
    @Setter
    private String topic;

    public RemoveChannelsFromPush(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNPushRemoveChannelResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.removePushNotificationsFromChannels(
                pushType,
                channels,
                deviceId,
                topic,
                environment
        ));
    }
}
