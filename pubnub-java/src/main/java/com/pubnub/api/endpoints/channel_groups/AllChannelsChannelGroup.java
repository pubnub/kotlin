package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import com.pubnub.api.models.server.Envelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroup extends Endpoint<Envelope<Object>, PNChannelGroupsAllChannelsResult> {
    @Setter
    private String channelGroup;

    public AllChannelsChannelGroup(PubNub pubnub,
                                   TelemetryManager telemetryManager,
                                   RetrofitManager retrofit,
                                   TokenManager tokenManager) {
        super(pubnub, telemetryManager, retrofit, tokenManager);
    }

    @Override
    protected List<String> getAffectedChannels() {
        return null;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return null;
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup == null || channelGroup.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_GROUP_MISSING).build();
        }
    }

    @Override
    protected Call<Envelope<Object>> doWork(Map<String, String> params) {
        return this.getRetrofit().getChannelGroupService()
                .allChannelsChannelGroup(this.getPubnub().getConfiguration().getSubscribeKey(), channelGroup, params);
    }

    @Override
    protected PNChannelGroupsAllChannelsResult createResponse(Response<Envelope<Object>> input) throws PubNubException {
        Map<String, Object> stateMappings;

        if (input.body() == null || input.body().getPayload() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        stateMappings = (Map<String, Object>) input.body().getPayload();
        List<String> channels = (ArrayList<String>) stateMappings.get("channels");

        return PNChannelGroupsAllChannelsResult.builder()
                .channels(channels)
                .build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNChannelsForGroupOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

}
