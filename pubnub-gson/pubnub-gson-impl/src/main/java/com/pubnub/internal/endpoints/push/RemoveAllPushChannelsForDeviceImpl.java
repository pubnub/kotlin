package com.pubnub.internal.endpoints.push;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveAllPushChannelsForDeviceImpl extends IdentityMappingEndpoint<PNPushRemoveAllChannelsResult> implements com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice {
    private PNPushType pushType;
    private String deviceId;
    private PNPushEnvironment environment = PNPushEnvironment.DEVELOPMENT;
    private String topic;

    public RemoveAllPushChannelsForDeviceImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<PNPushRemoveAllChannelsResult> createAction() {
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
