package com.pubnub.internal.endpoints.push;

import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveAllPushChannelsForDeviceImpl extends DelegatingEndpoint<PNPushRemoveAllChannelsResult> implements com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice {
    private PNPushType pushType;
    private String deviceId;
    private PNPushEnvironment environment;
    private String topic;

    public RemoveAllPushChannelsForDeviceImpl(CorePubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected RemoveAllPushChannelsForDeviceEndpoint createAction() {
        return pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
                pushType,
                deviceId,
                topic,
                environment
        );
    }
}
