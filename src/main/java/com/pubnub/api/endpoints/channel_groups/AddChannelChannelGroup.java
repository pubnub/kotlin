package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubnubUtil;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.server.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class AddChannelChannelGroup extends Endpoint<Envelope, Boolean> {
    @Setter private String channelGroup;
    @Setter private List<String> channels;


    public AddChannelChannelGroup(PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected void validateParams() throws PubNubException
    {
        if (channelGroup==null || channelGroup.isEmpty())
        {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_GROUP_MISSING).build();
        }
        if (channels.size() == 0)
        {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_CHANNEL_MISSING).build();
        }
    }

    @Override
    protected Call<Envelope> doWork(final Map<String, String> params) {
        ChannelGroupService service = this.createRetrofit().create(ChannelGroupService.class);

        if (channels.size() > 0) {
            params.put("add", PubnubUtil.joinString(channels, ","));
        }

        return service.AddChannelChannelGroup(pubnub.getConfiguration().getSubscribeKey(), channelGroup, params);
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
        return PNOperationType.PNAddChannelsToGroupOperation;
    }

}
