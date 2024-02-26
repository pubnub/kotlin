package com.pubnub.internal.endpoints.push;

import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.pubnub.internal.InternalPubNubClient;
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

    public AddChannelsToPushImpl(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @NotNull
    @Override
    protected com.pubnub.internal.endpoints.push.AddChannelsToPush createAction() {
        return pubnub.addPushNotificationsOnChannels(
                pushType,
                channels,
                deviceId,
                topic,
                environment
        );
    }
}
