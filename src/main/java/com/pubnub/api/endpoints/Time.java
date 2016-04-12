package com.pubnub.api.endpoints;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.consumer_facing.PNTimeResult;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

import java.util.List;

@Builder
public class Time extends Endpoint<List<Long>, PNTimeResult> {

    private Pubnub pubnub;

    private interface TimeService {
        @GET("/time/0")
        Call<List<Long>> fetchTime();
    }

    @Override
    protected final boolean validateParams() {
        return true;
    }

    @Override
    protected final Call<List<Long>> doWork() {
        TimeService service = this.createRetrofit(pubnub).create(TimeService.class);
        return service.fetchTime();
    }

    @Override
    protected final PNTimeResult createResponse(final Response<List<Long>> input) throws PubnubException {
        PNTimeResult timeData = new PNTimeResult();

        if (input.body() == null || input.body().size() == 0) {
            throw new PubnubException(PubnubError.PNERROBJ_PARSING_ERROR);
        }

        timeData.setTimetoken(input.body().get(0));
        return timeData;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNTimeOperation;
    }

}
