package com.pubnub.api.endpoints;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.consumer.PNTimeResult;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

public class Time extends Endpoint<List<Long>, PNTimeResult> {

    public Time(PubNub pubnub, TelemetryManager telemetryManager, RetrofitManager retrofit) {
        super(pubnub, telemetryManager, retrofit);
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

    }

    @Override
    protected Call<List<Long>> doWork(Map<String, String> params) {
        return this.getRetrofit().getTimeService().fetchTime(params);
    }

    @Override
    protected PNTimeResult createResponse(Response<List<Long>> input) throws PubNubException {
        PNTimeResult.PNTimeResultBuilder timeData = PNTimeResult.builder();

        if (input.body() == null || input.body().size() == 0) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        timeData.timetoken(input.body().get(0));
        return timeData.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNTimeOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return false;
    }

}
