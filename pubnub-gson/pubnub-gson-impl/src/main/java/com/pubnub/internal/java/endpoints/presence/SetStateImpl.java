package com.pubnub.internal.java.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.presence.SetState;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.internal.PubNubImpl;
import com.pubnub.internal.endpoints.presence.HeartbeatEndpoint;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Setter
@Accessors(chain = true, fluent = true)
public class SetStateImpl extends DelegatingEndpoint<Object, PNSetStateResult> implements SetState {
    @NotNull
    private List<String> channels = new ArrayList<>();
    @NotNull
    private List<String> channelGroups = new ArrayList<>();
    private Object state;
    @NotNull
    private String uuid = pubnub.getConfiguration().getUuid();
    private boolean withHeartbeat;


    public SetStateImpl(PubNubImpl pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (state == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_STATE_MISSING);
        }

        String stringifiedState = ((PubNubImpl) pubnub).getMapper().toJson(state);
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

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNSetStateResult> mapResult(@NotNull ExtendedRemoteAction<Object> action) {
        if (withHeartbeat) {
            return new MappingRemoteAction<>(action, result -> new PNSetStateResult(((PubNubImpl) pubnub).getMapper().toJsonTree(state)));
        } else {
            return new MappingRemoteAction<>(action, result -> (PNSetStateResult) result);
        }
    }

    @Override
    @NotNull
    protected Endpoint<Object> createAction() {
        if (!withHeartbeat) {
            // Regular way of setting presence explicitly is through SetPresenceState:
            return (Endpoint<Object>) (Object) pubnub.setPresenceState(
                    channels,
                    channelGroups,
                    state,
                    uuid
            );
        } else {
            // Some clients require alternative way of setting it through Heartbeat
            // Which is a feature brought over from the legacy Java SDK, and we need to be compatible:
            return (Endpoint<Object>) (Object) new HeartbeatEndpoint((PubNubImpl) pubnub, channels, channelGroups, composeStateParamValue());
        }
    }

    private Object composeStateParamValue() {
        Map<String, Object> stateParamValue = new HashMap<>();
        for (String channel : channels) {
            stateParamValue.put(channel, state);
        }
        return stateParamValue;
    }
}
