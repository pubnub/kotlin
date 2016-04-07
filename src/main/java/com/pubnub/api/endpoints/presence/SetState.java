package com.pubnub.api.endpoints.presence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pubnub.api.core.*;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.managers.StateManager;
import lombok.Builder;
import lombok.Singular;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;


@Builder
public class SetState extends Endpoint<Envelope<Object>, Boolean> {

    private Pubnub pubnub;
    private StateManager stateManager;
    @Singular private List<String> channels;
    @Singular private List<String> channelGroups;
    private Object state;

    @Override
    protected boolean validateParams() {

        if (state == null){
            return false;
        }

        if (channels.size() == 0 && channelGroups.size() == 0){
            return false;
        }


        return true;
    }

    @Override
    protected Call<Envelope<Object>> doWork() throws PubnubException {
        Map<String, Object> params = this.createBaseParams();
        ObjectWriter ow = new ObjectMapper().writer();
        String stringifiedState;

        PresenceService service = this.createRetrofit(this.pubnub).create(PresenceService.class);

        if (channelGroups.size() > 0){
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        try {
            stringifiedState = ow.writeValueAsString(state);
        } catch (JsonProcessingException e) {
            throw new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS, e.getMessage(), null);
        }

        params.put("state", stringifiedState);

        String channelCSV = channels.size() > 0 ? PubnubUtil.joinString(channels, ",") : ",";

        return service.setState(pubnub.getConfiguration().getSubscribeKey(), channelCSV, this.pubnub.getConfiguration().getUUID(), params);
    }

    @Override
    protected PnResponse<Boolean> createResponse(Response<Envelope<Object>> input) throws PubnubException {
        PnResponse<Boolean> pnResponse = new PnResponse<Boolean>();
        pnResponse.fillFromRetrofit(input);

        if (input.body().getStatus() == 200) {
            pnResponse.setPayload(true);

            for(String channel: channels) {
                stateManager.addStateForChannel(channel, state);
            }

        } else {
            pnResponse.setPayload(false);
        }

        return pnResponse;
    }
}
