package com.pubnub.api.endpoints.push;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Accessors(chain = true, fluent = true)
public class AddChannelsToPush extends Endpoint<PNPushAddChannelResult> {

    @Setter
    private PNPushType pushType;
    @Setter
    private List<String> channels;
    @Setter
    private String deviceId;
    @Setter
    private PNPushEnvironment environment = PNPushEnvironment.DEVELOPMENT;
    @Setter
    private String topic;

    public AddChannelsToPush(PubNubImpl pubnub) {
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
