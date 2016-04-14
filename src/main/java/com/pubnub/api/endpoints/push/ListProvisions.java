package com.pubnub.api.endpoints.push;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

@Builder
public class ListProvisions extends Endpoint<List<String>, List<String>> {

    private Pubnub pubnub;
    private PushType pushType;
    private String deviceId;

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<List<String>> doWork(Map<String, String> params) throws PubnubException {
        params.put("type", pushType.name().toLowerCase());
        PushService service = this.createRetrofit(pubnub).create(PushService.class);
        return service.listChannelsForDevice(pubnub.getConfiguration().getSubscribeKey(), deviceId, params);
    }

    @Override
    protected List<String> createResponse(Response<List<String>> input) throws PubnubException {
        return input.body();
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNPushNotificationListChannelsOperation;
    }

    @Override
    protected Pubnub getPubnub() {
        return pubnub;
    }

}
