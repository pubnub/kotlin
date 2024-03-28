package com.pubnub.internal.endpoints.push;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class AddChannelsToPushImpl extends DelegatingEndpoint<PNPushAddChannelResult> implements com.pubnub.api.endpoints.push.AddChannelsToPush {

    private PNPushType pushType;
    private List<String> channels;
    private String deviceId;
    private PNPushEnvironment environment = PNPushEnvironment.DEVELOPMENT;
    private String topic;

    public AddChannelsToPushImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNPushAddChannelResult> createAction() {
        return pubnub.addPushNotificationsOnChannels(
                pushType,
                channels,
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
        if (channels == null || channels.isEmpty()) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}
