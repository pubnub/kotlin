package com.pubnub.api.endpoints.push;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class RemoveAllPushChannelsForDevice extends Endpoint<PNPushRemoveAllChannelsResult> {
    @Setter
    private PNPushType pushType;
    @Setter
    private String deviceId;
    @Setter
    private PNPushEnvironment environment;
    @Setter
    private String topic;

    public RemoveAllPushChannelsForDevice(PubNubImpl pubnub) {
        super(pubnub);
    }
    @Override
    protected com.pubnub.internal.endpoints.push.RemoveAllPushChannelsForDevice createAction() {
        return pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
                pushType,
                deviceId,
                topic,
                environment
        );
    }
}
