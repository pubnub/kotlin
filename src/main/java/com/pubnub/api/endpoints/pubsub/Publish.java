package com.pubnub.api.endpoints.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubnub.api.core.*;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.PublishData;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.managers.PublishSequenceManager;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class Publish extends Endpoint<List<Object>, PublishData> {

    @Setter private Object message;
    @Setter private String channel;
    @Setter private Boolean shouldStore;
    @Setter private Boolean usePOST;
    @Setter private Object meta;

    PublishSequenceManager publishSequenceManager;

    public Publish(Pubnub pubnub, PublishSequenceManager providedPublishSequenceManager) {
        super(pubnub);

        this.publishSequenceManager = providedPublishSequenceManager;
    }

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
    protected final Call<List<Object>> doWork(Map<String, String> params) throws PubnubException {
        String stringifiedMessage;
        String stringifiedMeta;
        ObjectMapper mapper = new ObjectMapper();

        try {
            stringifiedMessage = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw PubnubException.builder().pubnubError(PubnubError.PNERROBJ_INVALID_ARGUMENTS).errormsg(e.getMessage()).build();
        }

        if (meta != null) {
            try {
                stringifiedMeta = mapper.writeValueAsString(meta);
                stringifiedMeta = PubnubUtil.urlEncode(stringifiedMeta);
                params.put("meta", stringifiedMeta);
            } catch (JsonProcessingException e) {
                throw PubnubException.builder().pubnubError(PubnubError.PNERROBJ_INVALID_ARGUMENTS).errormsg(e.getMessage()).build();
            }
        }

        if (shouldStore != null) {
            if (shouldStore) {
                params.put("store", "1");
            } else {
                params.put("store", "0");
            }
        }

        params.put("seqn", String.valueOf(publishSequenceManager.getNextSequence()));


        if (pubnub.getConfiguration().getCipherKey() != null) {
            Crypto crypto = new Crypto(pubnub.getConfiguration().getCipherKey());
            stringifiedMessage = crypto.encrypt(stringifiedMessage).replace("\n", "");
        }

        PubSubService service = this.createRetrofit().create(PubSubService.class);

        if (usePOST != null && usePOST) {
            Object payloadToSend;

            if (pubnub.getConfiguration().getCipherKey() != null) {
                payloadToSend = stringifiedMessage;
            } else {
                payloadToSend = message;
            }

            return service.publishWithPost(pubnub.getConfiguration().getPublishKey(),
                    pubnub.getConfiguration().getSubscribeKey(),
                    channel, payloadToSend, params);
        } else {

            if (pubnub.getConfiguration().getCipherKey() != null) {
                stringifiedMessage = "\"".concat(stringifiedMessage).concat("\"");
            }

            stringifiedMessage = PubnubUtil.urlEncode(stringifiedMessage);

            return service.publish(pubnub.getConfiguration().getPublishKey(),
                    pubnub.getConfiguration().getSubscribeKey(),
                    channel, stringifiedMessage, params);
        }
    }

    @Override
    protected final PublishData createResponse(final Response<List<Object>> input) throws PubnubException {
        PublishData publishData = new PublishData();
        publishData.setTimetoken(Long.valueOf(input.body().get(2).toString()));

        return publishData;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNPublishOperation;
    }

}
