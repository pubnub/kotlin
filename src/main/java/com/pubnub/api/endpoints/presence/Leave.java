package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.managers.StateManager;
import com.pubnub.api.managers.SubscriptionManager;
import lombok.Builder;
import lombok.Singular;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Builder
public class Leave extends Endpoint<Envelope, Boolean> {

    private Pubnub pubnub;
    private StateManager stateManager;
    private SubscriptionManager subscriptionManager;

    @Singular private Set<String> channels;
    @Singular private Set<String> channelGroups;

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope> doWork() {
        Map<String, Object> params = new HashMap<String, Object>();

        PresenceService service = this.createRetrofit(pubnub).create(PresenceService.class);

        params.put("uuid", pubnub.getConfiguration().getUUID());

        for (final String channel : channels) {
            stateManager.removeStateForChannel(channel);
            subscriptionManager.removeChannel(channel);
        }

        for (final String channelGroup : channelGroups) {
            subscriptionManager.removeChannelGroup(channelGroup);
        }

        return null;
    }

    @Override
    protected PnResponse<Boolean> createResponse(Response<Envelope> input) throws PubnubException {
        PnResponse<Boolean> pnResponse = new PnResponse<Boolean>();
        pnResponse.fillFromRetrofit(input);
        pnResponse.setPayload(true);

        return pnResponse;
    }
}
