package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.models.server_responses.SubscribeEnvelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import lombok.Singular;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supports calling of the subscribe endpoints and deconstructs the response to POJO's.
 */
@Builder
public class Subscribe extends Endpoint<SubscribeEnvelope, SubscribeEnvelope> {

    /**
     * Global {Pubnub} instance to fetch configuration.
     */
    private Pubnub pubnub;
    /**
     * List of channels that will be called to subscribe.
     */
    private @Singular List<String> channels;
    /**
     * List of channel groups that will be called with subscribe.
     */
    private @Singular List<String> channelGroups;

    /**
     * timeToken to subscribe with 0 for initial subscribe.
     */
    private String timetoken;

    @Override
    protected final boolean validateParams() {
        return true;
    }

    @Override
    protected final Call<SubscribeEnvelope> doWork() throws PubnubException {
        PubSubService pubSubService = this.createRetrofit(this.pubnub).create(PubSubService.class);
        String channelCSV;
        Map<String, Object> params = new HashMap<>();

        if (channelGroups.size() > 0) {
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        if (timetoken != null) {
            params.put("tt", timetoken);
        }

        if (channels.size() > 0) {
            channelCSV = PubnubUtil.joinString(channels, ",");
        } else {
            channelCSV = ",";
        }

        return pubSubService.subscribe(this.pubnub.getConfiguration().getSubscribeKey(), channelCSV,  params);
    }

    @Override
    protected final PnResponse<SubscribeEnvelope> createResponse(final Response<SubscribeEnvelope> input) throws PubnubException {
        PnResponse<SubscribeEnvelope> pnResponse = new PnResponse<>();
        pnResponse.fillFromRetrofit(input);

        if (input.body() != null && input.body() != null){
            pnResponse.setPayload(input.body());
        }

        return pnResponse;
    }
}
