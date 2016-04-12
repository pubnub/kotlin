package com.pubnub.api.endpoints.access;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;


@Builder
public class Grant extends Endpoint<Object, Object> {

    private Pubnub pubnub;

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Object> doWork() {
        return null;
    }

    @Override
    protected PnResponse<Object> createResponse(Response<Object> input) throws PubnubException {
        return null;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNAccessManagerGrant;
    }

}
