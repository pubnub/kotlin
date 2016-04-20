package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

@Builder
public class DeleteChannelGroup extends Endpoint<Envelope,Void>
{
    private Pubnub pubnub;
    private String group_name;
    private String uuid;

    @Override
    protected boolean validateParams() {return true;}

    @Override
    protected final Call<Envelope> doWork() throws PubnubException {
        Map<String, String> params = new HashMap<String, String>();

        if (group_name==null) throw new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS,"group cannot be null");

        ChannelGroupService service = this.createRetrofit(this.pubnub).create(ChannelGroupService.class);
        params.put("uuid", this.uuid != null ? this.uuid : pubnub.getConfiguration().getUuid());

        return service.DeleteChannelGroup(pubnub.getConfiguration().getSubscribeKey(), group_name , params);
    }

    @Override
    protected PnResponse createResponse(Response<Envelope> input) {
        PnResponse pnResponse = new PnResponse();
        pnResponse.fillFromRetrofit(input);
        pnResponse.setPayload("");

        return pnResponse;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }
}
