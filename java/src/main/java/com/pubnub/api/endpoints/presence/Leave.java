package com.pubnub.api.endpoints.presence;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.server.Envelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class Leave extends Endpoint<Envelope, Boolean> {
    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;

    public Leave(PubNub pubnub,
                 TelemetryManager telemetryManager,
                 RetrofitManager retrofit,
                 TokenManager tokenManager) {
        super(pubnub, telemetryManager, retrofit, tokenManager);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null || this.getPubnub().getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (channels.size() == 0 && channelGroups.size() == 0) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_AND_GROUP_MISSING).build();
        }
    }

    @Override
    protected Call<Envelope> doWork(Map<String, String> params) {
        String channelCSV;

        if (channelGroups.size() > 0) {
            params.put("channel-group", PubNubUtil.joinString(channelGroups, ","));
        }

        if (channels.size() > 0) {
            channelCSV = PubNubUtil.joinString(channels, ",");
        } else {
            channelCSV = ",";
        }

        return this.getRetrofit().getPresenceService().leave(this.getPubnub().getConfiguration().getSubscribeKey(),
                channelCSV, params);
    }

    @Override
    protected Boolean createResponse(Response<Envelope> input) throws PubNubException {
        return true;
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNUnsubscribeOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    protected List<String> getAffectedChannels() {
        return channels;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return channelGroups;
    }

}
