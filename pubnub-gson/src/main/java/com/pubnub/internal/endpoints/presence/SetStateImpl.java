package com.pubnub.internal.endpoints.presence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.presence.SetState;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@Setter
@Accessors(chain = true, fluent = true)
public class SetStateImpl extends DelegatingEndpoint<PNSetStateResult> implements SetState {
    private List<String> channels = new ArrayList<>();
    private List<String> channelGroups = new ArrayList<>();
    private Object state;
    private String uuid = pubnub.getConfiguration().getUuid();
    private boolean withHeartbeat;


    public SetStateImpl(CorePubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (withHeartbeat) {
            if (uuid != null && !uuid.equals(pubnub.getConfiguration().getUserId().getValue())) {
                // TODO bring back PubNub exception
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
                    new HeartbeatEndpoint(pubnub, channels, channelGroups, state), aBoolean -> new PNSetStateResult(pubnub.getMapper().toJsonTree(state)));
        }
    }
}
