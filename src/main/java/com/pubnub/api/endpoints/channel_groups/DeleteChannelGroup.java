package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult;
import com.pubnub.api.models.server.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class DeleteChannelGroup extends Endpoint<Envelope, Boolean> {
    @Setter private String channelGroup;

    public DeleteChannelGroup(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup==null || channelGroup.isEmpty())
        {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_GROUP_MISSING).build();
        }
    }

    @Override
    protected Call<Envelope> doWork(Map<String, String> params) {
        ChannelGroupService service = this.createRetrofit().create(ChannelGroupService.class);

        return service.DeleteChannelGroup(pubnub.getConfiguration().getSubscribeKey(), channelGroup, params);
    }

    @Override
    protected Boolean createResponse(Response<Envelope> input) throws PubNubException {
        return !input.body().isError();
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNRemoveGroupOperation;
    }

}
