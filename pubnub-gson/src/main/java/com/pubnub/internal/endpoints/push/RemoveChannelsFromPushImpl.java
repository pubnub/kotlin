package com.pubnub.internal.endpoints.push;

import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveChannelsFromPushImpl extends DelegatingEndpoint<PNPushRemoveChannelResult> implements com.pubnub.api.endpoints.push.RemoveChannelsFromPush {

    private PNPushType pushType;
    private List<String> channels;
    private String deviceId;
    private PNPushEnvironment environment;
    private String topic;

    public RemoveChannelsFromPushImpl(CorePubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected RemoveChannelsFromPushEndpoint createAction() {
        return pubnub.removePushNotificationsFromChannels(
                pushType,
                channels,
                deviceId,
                topic,
                environment
        );
    }
}
