package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.PubNub;
import com.pubnub.api.core.PubNubError;
import com.pubnub.api.core.PubNubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.server.Envelope;
import com.pubnub.api.core.models.server.presence.WhereNowPayload;
import com.pubnub.api.core.models.consumer_facing.PNPresenceWhereNowResult;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class WhereNow extends Endpoint<Envelope<WhereNowPayload>, PNPresenceWhereNowResult> {

    @Setter private String uuid;

    public WhereNow(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope<WhereNowPayload>> doWork(Map<String, String> params) {
        PresenceService service = this.createRetrofit().create(PresenceService.class);
        return service.whereNow(pubnub.getConfiguration().getSubscribeKey(),
                this.uuid != null ? this.uuid : pubnub.getConfiguration().getUuid(), params);
    }

    @Override
    protected PNPresenceWhereNowResult createResponse(Response<Envelope<WhereNowPayload>> input) throws PubNubException {
        if (input.body() == null || input.body().getPayload() == null) {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_PARSING_ERROR).build();
        }

        PNPresenceWhereNowResult pnPresenceWhereNowResult = new PNPresenceWhereNowResult();
        pnPresenceWhereNowResult.setChannels(input.body().getPayload().getChannels());

        return pnPresenceWhereNowResult;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNWhereNowOperation;
    }

}
