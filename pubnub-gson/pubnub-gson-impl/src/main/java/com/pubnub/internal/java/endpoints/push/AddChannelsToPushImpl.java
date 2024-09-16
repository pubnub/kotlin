package com.pubnub.internal.java.endpoints.push;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.push.AddChannelsToPush;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class AddChannelsToPushImpl extends PassthroughEndpoint<PNPushAddChannelResult> implements AddChannelsToPush {

    private PNPushType pushType;
    private List<String> channels;
    private String deviceId;
    private PNPushEnvironment environment = PNPushEnvironment.DEVELOPMENT;
    private String topic;

    public AddChannelsToPushImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNPushAddChannelResult> createRemoteAction() {
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
