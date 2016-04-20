package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
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
public class AllChannelsChannelGroup extends Endpoint<Envelope<Object>,Map<String, Object>>
{
    private Pubnub pubnub;
    private String group;
    private String uuid;


    @Override
    protected boolean validateParams() {return true;}

    @Override
    protected final Call<Envelope<Object>> doWork() throws PubnubException {
        Map<String, String> params = new HashMap<String, String>();

        if (group==null) throw new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS,"group cannot be null");

        ChannelGroupService service = this.createRetrofit(this.pubnub).create(ChannelGroupService.class);
        params.put("uuid", this.uuid != null ? this.uuid : pubnub.getConfiguration().getUuid());

        return service.AllChannelsChannelGroup(pubnub.getConfiguration().getSubscribeKey(), group , params);
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
