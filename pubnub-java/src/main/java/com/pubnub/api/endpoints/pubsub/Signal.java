package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.MapperManager;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.PNPublishResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class Signal extends Endpoint<List<Object>, PNPublishResult> {

    @Setter
    private Object message;

    @Setter
    private String channel;

    public Signal(PubNub pubnub,
                  TelemetryManager telemetryManager,
                  RetrofitManager retrofit,
                  TokenManager tokenManager) {
        super(pubnub, telemetryManager, retrofit, tokenManager);
    }

    @Override
    protected List<String> getAffectedChannels() {
        return Collections.singletonList(channel);
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return null;
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (message == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_MESSAGE_MISSING).build();
        }
        if (channel == null || channel.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING).build();
        }
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null || this.getPubnub()
                .getConfiguration()
                .getSubscribeKey()
                .isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (this.getPubnub().getConfiguration().getPublishKey() == null || this.getPubnub()
                .getConfiguration()
                .getPublishKey()
                .isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PUBLISH_KEY_MISSING).build();
        }
    }

    @Override
    protected Call<List<Object>> doWork(Map<String, String> params) throws PubNubException {
        MapperManager mapper = this.getPubnub().getMapper();

        String stringifiedMessage = mapper.toJson(message);

        params.putAll(encodeParams(params));

        stringifiedMessage = PubNubUtil.urlEncode(stringifiedMessage);

        return this.getRetrofit().getSignalService().signal(this.getPubnub().getConfiguration().getPublishKey(),
                this.getPubnub().getConfiguration().getSubscribeKey(),
                channel, stringifiedMessage, params);

    }

    @Override
    protected PNPublishResult createResponse(Response<List<Object>> input) throws PubNubException {
        PNPublishResult.PNPublishResultBuilder pnPublishResult = PNPublishResult.builder();
        pnPublishResult.timetoken(Long.valueOf(input.body().get(2).toString()));

        return pnPublishResult.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNSignalOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

}
