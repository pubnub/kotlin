package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import lombok.Singular;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

/**
 * Supports calling of the subscribe endpoints and deconstructs the response to POJO's.
 */
@Builder
public class Subscribe extends Endpoint<Object, Object> {

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
    private Long timetoken;

    @Override
    protected final boolean validateParams() {
        return true;
    }

    @Override
    protected final Call<Object> doWork() throws PubnubException {
        // TODO: implement me.
        return null;
    }

    @Override
    protected final PnResponse<Object> createResponse(final Response<Object> input) throws PubnubException {
        return null;
    }
}
