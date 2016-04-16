package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class GetState extends Endpoint<Envelope<Object>,Map<String, Object>> {

    @Setter private List<String> channels;
    @Setter private List<String> channelGroups;
    @Setter private String uuid;

    public GetState(Pubnub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected boolean validateParams() {
        if (uuid == null) {
            return false;
        }

        if (channels.size() == 0 && channelGroups.size() == 0) {
            return false;
        }

        return true;
    }

    @Override
    protected Call<Envelope<Object>> doWork(Map<String, String> params) throws PubnubException {
        PresenceService service = this.createRetrofit().create(PresenceService.class);

        if (channelGroups.size() > 0) {
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        String channelCSV = channels.size() > 0 ? PubnubUtil.joinString(channels, ",") : ",";

        return service.getState(pubnub.getConfiguration().getSubscribeKey(), channelCSV, uuid, params);
    }

    @Override
    protected Map<String, Object> createResponse(final Response<Envelope<Object>> input) throws PubnubException {
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
