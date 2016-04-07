package com.pubnub.api.endpoints;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.models.TimeData;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

import java.util.List;

@Builder
public class Time extends Endpoint<List<Long>, TimeData> {

    private Pubnub pubnub;

    interface TimeService {
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
    protected final PnResponse<TimeData> createResponse(final Response<List<Long>> input) {
        PnResponse<TimeData> pnResponse = new PnResponse<TimeData>();
        pnResponse.fillFromRetrofit(input);

        if (input.body() != null && input.body().size() > 0) {
            TimeData timeData = new TimeData();
            timeData.setTimetoken(input.body().get(0));
            pnResponse.setPayload(timeData);
        }

        return pnResponse;
    }

}
