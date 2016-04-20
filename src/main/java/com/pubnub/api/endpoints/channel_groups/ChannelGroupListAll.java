package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.endpoints.Endpoint;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;


@Builder
public class ChannelGroupListAll extends Endpoint<Envelope<Object>,Map<String, Object>>
{
    private Pubnub pubnub;
    private String uuid;

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected final Call<Envelope<Object>> doWork() throws PubnubException {
        Map<String, String> params = new HashMap<String, String>();

        ChannelGroupService service = this.createRetrofit(this.pubnub).create(ChannelGroupService.class);
        params.put("uuid", this.uuid != null ? this.uuid : pubnub.getConfiguration().getUuid());

        return service.ChannelGroupListAll(pubnub.getConfiguration().getSubscribeKey(), params);
    }

    @Override
    protected PnResponse<Map<String, Object>> createResponse(Response<Envelope<Object>> input) {
        PnResponse<Map<String, Object>> pnResponse = new PnResponse<Map<String, Object>>();
        pnResponse.fillFromRetrofit(input);

        if (input.body() != null && input.body().getPayload()!=null) {
            Map<String, Object> stateMappings;
            stateMappings = (Map<String, Object>) input.body().getPayload();
            pnResponse.setPayload(stateMappings);
        }

        return pnResponse;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

}
