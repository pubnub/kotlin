package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.WhereNowData;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;


@Builder
public class WhereNow extends Endpoint<Envelope<WhereNowData>, WhereNowData> {

    private Pubnub pubnub;
    private String uuid;


    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope<WhereNowData>> doWork() {
        PresenceService service = this.createRetrofit(pubnub).create(PresenceService.class);
        return service.whereNow(pubnub.getConfiguration().getSubscribeKey(),
                this.uuid != null ? this.uuid : pubnub.getConfiguration().getUUID());
    }

    @Override
    protected PnResponse<WhereNowData> createResponse(Response<Envelope<WhereNowData>> input) {
        PnResponse<WhereNowData> pnResponse = new PnResponse<WhereNowData>();
        pnResponse.fillFromRetrofit(input);

        if (input.body() != null && input.body().getPayload() != null){
            pnResponse.setPayload(input.body().getPayload());
        }

        return pnResponse;
    }

}
