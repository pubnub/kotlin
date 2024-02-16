package com.pubnub.api.endpoints.presence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.internal.PubNubImpl;
import com.pubnub.internal.endpoints.presence.Heartbeat;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@Accessors(chain = true, fluent = true)
public class SetState extends Endpoint<PNSetStateResult> {
    @Setter
    private List<String> channels = new ArrayList<>();
    @Setter
    private List<String> channelGroups = new ArrayList<>();
    @Setter
    private Object state;
    @Setter
    private String uuid = pubnub.getConfiguration().getUuid();
    @Setter
    private boolean withHeartbeat;


    public SetState(PubNubImpl pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (withHeartbeat) {
            if (uuid != null && !uuid.equals(pubnub.getConfiguration().getUserId().getValue())) {
                // TODO potentially bring back PubNub exception
                throw new IllegalStateException("UserId can't be different from UserId in configuration when flag withHeartbeat is set to true");
            }
        }
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
            return new MappingRemoteAction<>(
                    new Heartbeat(pubnub, channels, channelGroups, state), aBoolean -> new PNSetStateResult(pubnub.getMapper().toJsonTree(state)));
        }
    }
}
