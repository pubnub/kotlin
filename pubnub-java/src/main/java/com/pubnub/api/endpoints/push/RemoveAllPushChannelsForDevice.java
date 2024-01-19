package com.pubnub.api.endpoints.push;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveAllPushChannelsForDevice extends ValidatingEndpoint<PNPushRemoveAllChannelsResult> {
    private PNPushType pushType;
    private String deviceId;
    private PNPushEnvironment environment;
    private String topic;

    public RemoveAllPushChannelsForDevice(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }
    @Override
    protected Endpoint<PNPushRemoveAllChannelsResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
                pushType,
                deviceId,
                topic,
                environment
        ));
    }
}
