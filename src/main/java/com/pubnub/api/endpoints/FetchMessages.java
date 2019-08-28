package com.pubnub.api.endpoints;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.MapperManager;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.server.FetchMessagesEnvelope;
import com.pubnub.api.models.server.HistoryForChannelsItem;
import com.pubnub.api.vendor.Crypto;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class FetchMessages extends Endpoint<FetchMessagesEnvelope, PNFetchMessagesResult> {
    private static final int MAX_MESSAGES = 25;
    @Setter
    private List<String> channels;
    @Setter
    private Integer maximumPerChannel;
    @Setter
    private Long start;
    @Setter
    private Long end;

    public FetchMessages(PubNub pubnub, TelemetryManager telemetryManager, RetrofitManager retrofit) {
        super(pubnub, telemetryManager, retrofit);
        channels = new ArrayList<>();
        maximumPerChannel = 1;
    }

    @Override
    protected List<String> getAffectedChannels() {
        return channels;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return null;
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
    protected Call<FetchMessagesEnvelope> doWork(Map<String, String> params) {
        params.put("max", String.valueOf(maximumPerChannel));

        if (start != null) {
            params.put("start", Long.toString(start).toLowerCase());
        }
        if (end != null) {
            params.put("end", Long.toString(end).toLowerCase());
        }

        return this.getRetrofit().getHistoryService().fetchMessages(this.getPubnub().getConfiguration().getSubscribeKey(), PubNubUtil.joinString(channels, ","), params);
    }

    @Override
    protected PNFetchMessagesResult createResponse(Response<FetchMessagesEnvelope> input) throws PubNubException {
        if (input.body() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        PNFetchMessagesResult.PNFetchMessagesResultBuilder result = PNFetchMessagesResult.builder();
        Map<String, List<PNMessageResult>> listMap = new HashMap<>();

        FetchMessagesEnvelope envelope = input.body();

        for (Map.Entry<String, List<HistoryForChannelsItem>> entry : envelope.getChannels().entrySet()) {

            List<PNMessageResult> messages = new ArrayList<>();

            for (HistoryForChannelsItem item : entry.getValue()) {
                BasePubSubResult.BasePubSubResultBuilder resultBuilder = BasePubSubResult.builder();
                resultBuilder.channel(entry.getKey());
                JsonElement message = processMessage(item.getMessage());
                resultBuilder.timetoken(item.getTimetoken());
                messages.add(new PNMessageResult(resultBuilder.build(), message));
            }

            listMap.put(entry.getKey(), messages);
        }

        result.channels(listMap);

        return result.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNFetchMessagesOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    private JsonElement processMessage(JsonElement message) throws PubNubException {
        // if we do not have a crypto key, there is no way to process the node; let's return.
        if (this.getPubnub().getConfiguration().getCipherKey() == null) {
            return message;
        }

        Crypto crypto = new Crypto(this.getPubnub().getConfiguration().getCipherKey());
        MapperManager mapper = this.getPubnub().getMapper();
        String inputText;
        String outputText;
        JsonElement outputObject;

        if (mapper.isJsonObject(message) && mapper.hasField(message, "pn_other")) {
            inputText = mapper.elementToString(message, "pn_other");
        } else {
            inputText = mapper.elementToString(message);
        }

        outputText = crypto.decrypt(inputText);
        outputObject = mapper.fromJson(outputText, JsonElement.class);

        // inject the decoded resposne into the payload
        if (mapper.isJsonObject(message) && mapper.hasField(message, "pn_other")) {
            JsonObject objectNode = mapper.getAsObject(message);
            mapper.putOnObject(objectNode, "pn_other", outputObject);
            outputObject = objectNode;
        }

        return outputObject;
    }

}

