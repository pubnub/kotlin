package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.core.*;
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
public class RemoveChannel extends Endpoint<Envelope,Void>
{
    private Pubnub pubnub;
    private String group;
    @Singular
    private List<String> channels;
    private String uuid;

    @Override
    protected boolean validateParams() {return true;}

    @Override
    protected final Call<Envelope> doWork() throws PubnubException {
        Map<String, String> params = new HashMap<String, String>();
        String channelCSV;

        if (group==null) throw new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS,"group cannot be null");
        if (channels.size() == 0) throw new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS,"channel cannot be null");

        ChannelGroupService service = this.createRetrofit(this.pubnub).create(ChannelGroupService.class);

        channelCSV = PubnubUtil.joinString(channels, ",");

        params.put("remove", channelCSV);
        params.put("uuid", this.uuid != null ? this.uuid : pubnub.getConfiguration().getUuid());

        return service.RemoveChannel(pubnub.getConfiguration().getSubscribeKey(), group , params);
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
