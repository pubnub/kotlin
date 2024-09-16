package com.pubnub.internal.java.endpoints.push;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.push.RemoveAllPushChannelsForDevice;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveAllPushChannelsForDeviceImpl extends PassthroughEndpoint<PNPushRemoveAllChannelsResult> implements RemoveAllPushChannelsForDevice {
    private PNPushType pushType;
    private String deviceId;
    private PNPushEnvironment environment = PNPushEnvironment.DEVELOPMENT;
    private String topic;

    public RemoveAllPushChannelsForDeviceImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNPushRemoveAllChannelsResult> createRemoteAction() {
        return pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
                pushType,
                deviceId,
                topic,
                environment
        );
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (pushType == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_PUSH_TYPE_MISSING);
        }
        if (deviceId == null || deviceId.isEmpty()) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_DEVICE_ID_MISSING);
        }
    }
}
