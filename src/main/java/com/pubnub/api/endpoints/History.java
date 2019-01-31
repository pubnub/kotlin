package com.pubnub.api.endpoints;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.MapperManager;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.vendor.Crypto;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.*;

@Accessors(chain = true, fluent = true)
public class History extends Endpoint<JsonElement, PNHistoryResult> {
    private static final int MAX_COUNT = 100;
    @Setter
    private String channel;
    @Setter
    private Long start;
    @Setter
    private Long end;
    @Setter
    private Boolean reverse;
    @Setter
    private Integer count;
    @Setter
    private Boolean includeTimetoken;

    public History(PubNub pubnub, TelemetryManager telemetryManager, RetrofitManager retrofit) {
        super(pubnub, telemetryManager, retrofit);
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
        if (channel == null || channel.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING).build();
        }
    }

    @Override
    protected Call<JsonElement> doWork(Map<String, String> params) {

        if (reverse != null) {
            params.put("reverse", String.valueOf(reverse));
        }

        if (includeTimetoken != null) {
            params.put("include_token", String.valueOf(includeTimetoken));
        }

        if (count != null && count > 0 && count <= MAX_COUNT) {
            params.put("count", String.valueOf(count));
        } else {
            params.put("count", "100");
        }

        if (start != null) {
            params.put("start", Long.toString(start).toLowerCase());
        }
        if (end != null) {
            params.put("end", Long.toString(end).toLowerCase());
        }

        return this.getRetrofit().getHistoryService().fetchHistory(this.getPubnub().getConfiguration()
                .getSubscribeKey(), channel, params);
    }

    @Override
    protected PNHistoryResult createResponse(Response<JsonElement> input) throws PubNubException {
        PNHistoryResult.PNHistoryResultBuilder historyData = PNHistoryResult.builder();
        List<PNHistoryItemResult> messages = new ArrayList<>();
        MapperManager mapper = getPubnub().getMapper();

        if (input.body() != null) {
            Long startTimeToken = mapper.elementToLong(mapper.getArrayElement(input.body(), 1));
            Long endTimeToken = mapper.elementToLong(mapper.getArrayElement(input.body(), 2));

            historyData.startTimetoken(startTimeToken);
            historyData.endTimetoken(endTimeToken);


            if (mapper.getArrayElement(input.body(), 0).isJsonArray()) {
                Iterator<JsonElement> it = mapper.getArrayIterator(mapper.getArrayElement(input.body(), 0));
                while (it.hasNext()) {
                    JsonElement historyEntry = it.next();
                    PNHistoryItemResult.PNHistoryItemResultBuilder historyItem = PNHistoryItemResult.builder();
                    JsonElement message;

                    if (includeTimetoken != null && includeTimetoken) {
                        historyItem.timetoken(mapper.elementToLong(historyEntry, "timetoken"));
                        message = processMessage(mapper.getField(historyEntry, "message"));
                    } else {
                        message = processMessage(historyEntry);
                    }

                    historyItem.entry(message);
                    messages.add(historyItem.build());
                }
            } else {
                throw PubNubException.builder()
                        .pubnubError(PubNubErrorBuilder.PNERROBJ_HTTP_ERROR)
                        .errormsg("History is disabled")
                        .jso(input.body())
                        .build();
            }


            historyData.messages(messages);
        }

        return historyData.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNHistoryOperation;
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
        MapperManager mapper = getPubnub().getMapper();
        String inputText;
        String outputText;
        JsonElement outputObject;

        if (mapper.isJsonObject(message) && mapper.hasField(message, "pn_other")) {
            inputText = mapper.elementToString(message, "pn_other");
        } else {
            inputText = mapper.elementToString(message);
        }

        outputText = crypto.decrypt(inputText);
        outputObject = this.getPubnub().getMapper().fromJson(outputText, JsonElement.class);

        // inject the decoded response into the payload
        if (mapper.isJsonObject(message) && mapper.hasField(message, "pn_other")) {
            JsonObject objectNode = mapper.getAsObject(message);
            mapper.putOnObject(objectNode, "pn_other", outputObject);
            outputObject = objectNode;
        }

        return outputObject;
    }

}
