package com.pubnub.api.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.internal.endpoints.presence.Heartbeat;
import kotlin.jvm.functions.Function1;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@Setter
@Accessors(chain = true, fluent = true)
public class SetState extends ValidatingEndpoint<PNSetStateResult> {
    private List<String> channels;
    private List<String> channelGroups;
    private Object state;
    private String uuid;
    private boolean withHeartbeat;


    public SetState(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
        uuid = pubnub.getConfiguration().getUuid();
    }

    @Override
    protected Endpoint<PNSetStateResult> createAction() {
        if (!withHeartbeat) {
            return new IdentityMappingEndpoint<>(pubnub.setPresenceState(
                    channels,
                    channelGroups,
                    state,
                    uuid
            ));
        } else {
            if (uuid != null && !uuid.equals(pubnub.getConfiguration().getUserId().getValue())) {
                // TODO potentially bring back PubNub exception
                throw new IllegalStateException("UserId can't be different from UserId in configuration when flag withHeartbeat is set to true");
            }
            return new MappingEndpoint<>(
                    new Heartbeat(pubnub, channels, channelGroups, state), aBoolean -> new PNSetStateResult(pubnub.getMapper().toJsonTree(state)));
        }
    }
}
