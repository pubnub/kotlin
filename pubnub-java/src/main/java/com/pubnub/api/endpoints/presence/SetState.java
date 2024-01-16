package com.pubnub.api.endpoints.presence;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.internal.endpoints.presence.Heartbeat;
import kotlin.jvm.functions.Function1;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@Accessors(chain = true, fluent = true)
public class SetState extends Endpoint<PNSetStateResult> {
    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private Object state;
    @Setter
    private String uuid;
    @Setter
    private boolean withHeartbeat;


    public SetState(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    protected ExtendedRemoteAction<PNSetStateResult> createAction() {
        if (!withHeartbeat) {
            return pubnub.setPresenceState(
                    channels,
                    channelGroups,
                    state,
                    uuid
            );
        } else {
            if (uuid != null && !uuid.equals(pubnub.getConfiguration().getUserId().getValue())) {
                // TODO potentially bring back PubNub exception
                throw new IllegalStateException("UserId can't be different from UserId in configuration when flag withHeartbeat is set to true");
            }
            return new MappingRemoteAction<>(
                    new Heartbeat(pubnub, channels, channelGroups, state), new Function1<Boolean, PNSetStateResult>() {
                @Override
                public PNSetStateResult invoke(Boolean aBoolean) {
                    return new PNSetStateResult(pubnub.getMapper().toJsonTree(state));
                }
            });
        }
    }
}
