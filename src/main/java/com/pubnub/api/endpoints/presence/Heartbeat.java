package com.pubnub.api.endpoints.presence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class Heartbeat extends Endpoint<Envelope, Boolean> {

    @Setter private Object state;
    @Setter private List<String> channels;
    @Setter private List<String> channelGroups;

    public Heartbeat(Pubnub pubnub) {
        super(pubnub);
    }

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope> doWork(Map<String, String> params) throws PubnubException {
        ObjectWriter ow = new ObjectMapper().writer();

        params.put("heartbeat", String.valueOf(pubnub.getConfiguration().getPresenceTimeout()));

        if (channelGroups != null && channelGroups.size() > 0) {
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        String channelsCSV;

        if (channels != null && channels.size() > 0) {
            channelsCSV = PubnubUtil.joinString(channels, ",");
        } else {
            channelsCSV = ",";
        }

        if (state != null) {
            String stringifiedState;

            try {
                stringifiedState = ow.writeValueAsString(state);
            } catch (JsonProcessingException e) {
                throw PubnubException.builder().pubnubError(PubnubError.PNERROBJ_INVALID_ARGUMENTS).errormsg(e.getMessage()).build();
            }

            stringifiedState = PubnubUtil.urlEncode(stringifiedState);
            params.put("state", stringifiedState);
        }

        PresenceService service = this.createRetrofit().create(PresenceService.class);
        return service.heartbeat(pubnub.getConfiguration().getSubscribeKey(), channelsCSV, params);
    }

    @Override
    protected Boolean createResponse(Response<Envelope> input) throws PubnubException {
        return true;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNHeartbeatOperation;
    }

}