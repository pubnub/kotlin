package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.managers.StateManager;
import com.pubnub.api.managers.SubscriptionManager;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

@Builder
public class Heartbeat extends Endpoint<Envelope, Boolean> {

    private Pubnub pubnub;
    private StateManager stateManager;
    private SubscriptionManager subscriptionManager;

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope> doWork() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("uuid", pubnub.getConfiguration().getUUID());
        params.put("state", stateManager.getStringifiedStateByChannel());
        params.put("heartbeat", pubnub.getConfiguration().getPresenceTimeout());

        if (subscriptionManager.getSubscribedChannelGroups().size() > 0){
            params.put("channel-group", PubnubUtil.joinString(subscriptionManager.getSubscribedChannelGroups(), ","));
        }

        String channels;

        if (subscriptionManager.getSubscribedChannels().size() > 0){
            channels = PubnubUtil.joinString(subscriptionManager.getSubscribedChannels(), ",");
        } else {
            channels = ",";
        }

        PresenceService service = this.createRetrofit(pubnub).create(PresenceService.class);
        return service.heartbeat(pubnub.getConfiguration().getSubscribeKey(), channels, params);
    }

    @Override
    protected PnResponse<Boolean> createResponse(Response<Envelope> input) throws PubnubException {
        PnResponse<Boolean> pnResponse = new PnResponse<Boolean>();
        pnResponse.fillFromRetrofit(input);
        pnResponse.setPayload(true);

        return pnResponse;
    }
}