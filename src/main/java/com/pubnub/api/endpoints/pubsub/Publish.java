package com.pubnub.api.endpoints.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.PublishData;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

@Builder
public class Publish extends Endpoint<List<Object>, PublishData> {

    private Pubnub pubnub;
    private Object message;
    private String channel;
    private Boolean shouldStore;
    private Boolean usePOST;
    private Object meta;

    @Override
    protected final boolean validateParams() {
        if (message == null) {
            return false;
        }

        if (channel == null || channel.length() == 0) {
            return false;
        }

        return true;
    }

    @Override
    protected final Call<List<Object>> doWork() throws PubnubException {
        Map<String, Object> params = this.createBaseParams();
        String stringifiedMessage;
        String stringifiedMeta;
        ObjectWriter ow = new ObjectMapper().writer();
        try {
            stringifiedMessage = ow.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS, e.getMessage(), null);
        }

        if (meta != null) {
            try {
                stringifiedMeta = ow.writeValueAsString(meta);
                params.put("meta", stringifiedMeta);
            } catch (JsonProcessingException e) {
                throw new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS, e.getMessage(), null);
            }
        }

        if (pubnub.getConfiguration().getAuthKey() != null) {
            params.put("auth", pubnub.getConfiguration().getAuthKey());
        }

        params.put("uuid", pubnub.getConfiguration().getUuid());

        if (shouldStore != null) {
            params.put("store", shouldStore);
        }

        PubSubService service = this.createRetrofit(pubnub).create(PubSubService.class);


        if (usePOST != null && usePOST) {
            return service.publishWithPost(pubnub.getConfiguration().getPublishKey(),
                    pubnub.getConfiguration().getSubscribeKey(),
                    channel, message, params);
        } else {
            return service.publish(pubnub.getConfiguration().getPublishKey(),
                    pubnub.getConfiguration().getSubscribeKey(),
                    channel, stringifiedMessage, params);
        }
    }

    @Override
    protected final PnResponse<PublishData> createResponse(final Response<List<Object>> input) throws PubnubException {
        PnResponse<PublishData> pnResponse = new PnResponse<PublishData>();
        pnResponse.fillFromRetrofit(input);
        PublishData publishData = new PublishData();
        publishData.setTimetoken(Long.valueOf(input.body().get(2).toString()));

        return pnResponse;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

}
