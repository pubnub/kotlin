package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
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
public class Leave extends Endpoint<Envelope, Boolean> {

    private Pubnub pubnub;

    @Singular private List<String> channels;
    @Singular private List<String> channelGroups;

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope> doWork() {
        Map<String, Object> params = new HashMap<>();
        String channelCSV;
        PresenceService service = this.createRetrofit(pubnub).create(PresenceService.class);

        params.put("uuid", pubnub.getConfiguration().getUuid());

        if (channelGroups.size() > 0) {
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        if (channels.size() > 0) {
            channelCSV = PubnubUtil.joinString(channels, ",");
        } else {
            channelCSV = ",";
        }

        return service.leave(this.pubnub.getConfiguration().getSubscribeKey(), channelCSV, params);
    }

    @Override
    protected PnResponse<Boolean> createResponse(Response<Envelope> input) throws PubnubException {
        PnResponse<Boolean> pnResponse = new PnResponse<>();
        pnResponse.fillFromRetrofit(input);
        pnResponse.setPayload(true);

        return pnResponse;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

}
