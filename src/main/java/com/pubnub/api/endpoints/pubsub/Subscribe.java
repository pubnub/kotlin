package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.server_responses.SubscribeEnvelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

/**
 * Supports calling of the subscribe endpoints and deconstructs the response to POJO's.
 */
@Builder
@Slf4j
public class Subscribe extends Endpoint<SubscribeEnvelope, SubscribeEnvelope> {


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
    private Long timetoken;

    @Override
    protected final boolean validateParams() {
        return true;
    }

    @Override
    protected final Call<SubscribeEnvelope> doWork(Map<String, String> params) throws PubnubException {
        PubSubService pubSubService = this.createRetrofit(this.pubnub).create(PubSubService.class);
        String channelCSV;

        if (channelGroups.size() > 0) {
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        if (timetoken != null) {
            params.put("tt", timetoken.toString());
        }

        if (channels.size() > 0) {
            channelCSV = PubnubUtil.joinString(channels, ",");
        } else {
            channelCSV = ",";
        }

        return pubSubService.subscribe(this.pubnub.getConfiguration().getSubscribeKey(), channelCSV,  params);
    }

    @Override
    protected final SubscribeEnvelope createResponse(final Response<SubscribeEnvelope> input) throws PubnubException {

        if (input.body() == null) {
            throw new PubnubException(PubnubError.PNERROBJ_PARSING_ERROR);
        }

        SubscribeEnvelope subscribeEnvelope = input.body();
        return subscribeEnvelope;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getSubscribeTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return null;
    }

    @Override
    protected Pubnub getPubnub() {
        return pubnub;
    }

}
