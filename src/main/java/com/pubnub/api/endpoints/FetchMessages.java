package com.pubnub.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.MapperManager;
import com.pubnub.api.models.server.HistoryForChannelsEnvelope;
import com.pubnub.api.models.server.HistoryForChannelsItem;
import com.pubnub.api.vendor.Crypto;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class FetchMessages extends Endpoint<HistoryForChannelsEnvelope, HistoryForChannelsEnvelope> {
    private static final int MAX_MESSAGES = 25;
    @Setter
    private List<String> channels;
    @Setter
    private Integer maximumPerChannel;
    @Setter
    private Long start;
    @Setter
    private Long end;

    public FetchMessages(PubNub pubnub, Retrofit retrofit) {
        super(pubnub, retrofit);
        channels = new ArrayList<>();
        maximumPerChannel = 1;
    }

    private interface HistoryForChannelsService {
        @GET("v3/history/sub-key/{subKey}/channel/{channels}")
        Call<HistoryForChannelsEnvelope> fetchHistoryForChannels(@Path("subKey") String subKey,
                                                                 @Path("channels") String channels,
                                                                 @QueryMap Map<String, String> options);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channels == null || channels.size() == 0) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING).build();
        }
        if (maximumPerChannel != null && maximumPerChannel > MAX_MESSAGES) {
            maximumPerChannel = MAX_MESSAGES;
        } else if (maximumPerChannel == null) {
            maximumPerChannel = 1;
        }
    }

    @Override
    protected Call<HistoryForChannelsEnvelope> doWork(Map<String, String> params) {

        HistoryForChannelsService service = this.getRetrofit().create(HistoryForChannelsService.class);

        params.put("max", String.valueOf(maximumPerChannel));

        if (start != null) {
            params.put("start", Long.toString(start).toLowerCase());
        }
        if (end != null) {
            params.put("end", Long.toString(end).toLowerCase());
        }

        return service.fetchHistoryForChannels(this.getPubnub().getConfiguration().getSubscribeKey(), PubNubUtil.joinString(channels, ","), params);
    }

    @Override
    protected HistoryForChannelsEnvelope createResponse(Response<HistoryForChannelsEnvelope> input) throws PubNubException {
        if (input.body() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        HistoryForChannelsEnvelope envelope = input.body();
        Map<String, List<HistoryForChannelsItem>> channelsList = envelope.getChannels();

        for (Map.Entry<String, List<HistoryForChannelsItem>> entry : channelsList.entrySet()) {
            for (HistoryForChannelsItem item: entry.getValue()) {
                JsonNode message = processMessage(item.getMessage());
                item.setMessage(message);
            }

        }

        return envelope;
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNFetchMessagesOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    private JsonNode processMessage(JsonNode message) throws PubNubException {
        // if we do not have a crypto key, there is no way to process the node; let's return.
        if (this.getPubnub().getConfiguration().getCipherKey() == null) {
            return message;
        }

        Crypto crypto = new Crypto(this.getPubnub().getConfiguration().getCipherKey());
        MapperManager mapper = this.getPubnub().getMapper();
        String inputText;
        String outputText;
        JsonNode outputObject;

        if (message.isObject() && message.has("message")) {
            inputText = message.get("message").asText();
        } else {
            inputText = message.asText();
        }

        outputText = crypto.decrypt(inputText);
        outputObject = mapper.fromJson(outputText, JsonNode.class);

        // inject the decoded response into the payload
        if (message.isObject() && message.has("message")) {
            ObjectNode objectNode = (ObjectNode) message;
            objectNode.set("message", outputObject);
            outputObject = objectNode;
        }

        return outputObject;
    }

}

