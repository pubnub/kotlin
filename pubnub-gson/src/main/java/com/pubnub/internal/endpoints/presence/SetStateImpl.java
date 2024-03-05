package com.pubnub.internal.endpoints.presence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.presence.SetState;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONException;
import org.json.JSONObject;

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


    public SetStateImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (state == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_STATE_MISSING);
        }

        String stringifiedState = pubnub.getMapper().toJson(state);
        if (!isJsonObject(stringifiedState)) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_STATE_MUST_BE_JSON_OBJECT);
        }
        if (withHeartbeat) {
            if (uuid != null && !uuid.equals(pubnub.getConfiguration().getUserId().getValue())) {
                throw new PubNubException(PubNubErrorBuilder.PNERROBJ_USERID_CAN_NOT_BE_DIFFERENT_FROM_IN_CONFIGURATION_WHEN_WITHHEARTBEAT_TRUE);
            }
        }
    }

    private boolean isJsonObject(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            return false;
        }
        return true;
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
