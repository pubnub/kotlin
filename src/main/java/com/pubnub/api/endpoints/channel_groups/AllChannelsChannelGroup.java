package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.consumer_facing.PNChannelGroupsAllChannelsResult;
import com.pubnub.api.endpoints.Endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroup extends Endpoint<Envelope<Object>,PNChannelGroupsAllChannelsResult>
{
    @Setter private String uuid;
    @Setter private String group;

    public AllChannelsChannelGroup(Pubnub pubnub) {
        super(pubnub);
    }

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope<Object>> doWork(Map<String, String> params) {
        ChannelGroupService service = this.createRetrofit().create(ChannelGroupService.class);
        params.put("uuid", this.uuid != null ? this.uuid : pubnub.getConfiguration().getUuid());

        return service.AllChannelsChannelGroup(pubnub.getConfiguration().getSubscribeKey(), group, params);
    }

    @Override
    protected PNChannelGroupsAllChannelsResult createResponse(Response<Envelope<Object>> input) throws PubnubException {
        Map<String, Object> stateMappings;

        if (input.body() == null || input.body().getPayload() == null) {
            throw PubnubException.builder().pubnubError(PubnubError.PNERROBJ_PARSING_ERROR).build();
        }

        PNChannelGroupsAllChannelsResult pnChannelGroupsAllChannelsResult = new PNChannelGroupsAllChannelsResult();

        stateMappings = (Map<String, Object>) input.body().getPayload();
        List<String> channels = (ArrayList<String>) stateMappings.get("channels");
        pnChannelGroupsAllChannelsResult.setChannels(channels);

        return pnChannelGroupsAllChannelsResult;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNChannelsForGroupOperation;
    }

}
