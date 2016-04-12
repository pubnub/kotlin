package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import lombok.Singular;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class GetState extends Endpoint<Envelope<Object>,Map<String, Object>> {

    private Pubnub pubnub;
    @Singular private List<String> channels;
    @Singular private List<String> channelGroups;
    private String uuid;

    @Override
    protected boolean validateParams() {
        if (uuid == null){
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
        PresenceService service = this.createRetrofit(this.pubnub).create(PresenceService.class);

        if (channelGroups.size() > 0){
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        String channelCSV = channels.size() > 0 ? PubnubUtil.joinString(channels, ",") : ",";

        return service.getState(pubnub.getConfiguration().getSubscribeKey(), channelCSV, uuid, params);
    }

    @Override
    protected Map<String, Object> createResponse(Response<Envelope<Object>> input) throws PubnubException {
        Map<String, Object> stateMappings;

        if (channels.size() == 1 && channelGroups.size() == 0) {
            stateMappings = new HashMap<>();
            stateMappings.put(channels.get(0), input.body().getPayload());
        } else {
            stateMappings = (Map<String, Object>) input.body().getPayload();
        }

        return stateMappings;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetState;
    }

}
