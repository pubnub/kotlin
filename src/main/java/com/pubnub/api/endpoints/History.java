package com.pubnub.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.pubnub.api.core.Crypto;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.consumer_facing.PNHistoryResult;
import com.pubnub.api.core.models.consumer_facing.PNHistoryItemResult;
import lombok.Builder;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Builder
public class History extends Endpoint<JsonNode, PNHistoryResult> {

    private Pubnub pubnub;
    private String channel;
    private Long start;
    private Long end;
    private boolean reverse = false;
    private Integer count;
    private boolean includeTimetoken = false;

    private interface HistoryService {
        @GET("v2/history/sub-key/{subKey}/channel/{channel}")
        Call<JsonNode> fetchHistory(@Path("subKey") String subKey,
                                    @Path("channel") String channel,
                                    @QueryMap Map<String, String> options);
    }

    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<JsonNode> doWork() {
        Map<String, String> params = new HashMap<String, String>();

        HistoryService service = this.createRetrofit(pubnub).create(HistoryService.class);

        params.put("reverse", String.valueOf(reverse));
        params.put("include_token", String.valueOf(includeTimetoken));

        if (count != null && count > 0 && count <= 10) {
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

        return service.fetchHistory(pubnub.getConfiguration().getSubscribeKey(), channel, params);
    }

    @Override
    protected PNHistoryResult createResponse(Response<JsonNode> input) throws PubnubException {
        PNHistoryResult historyData = new PNHistoryResult();

        if (input.body() != null) {
            historyData.setStartTimeToken(input.body().get(1).asLong());
            historyData.setEndTimeToken(input.body().get(2).asLong());

            ArrayNode historyItems = (ArrayNode) input.body().get(0);

            for (final JsonNode historyItem : historyItems) {
                PNHistoryItemResult historyItemData = new PNHistoryItemResult();
                Object message;

                if (includeTimetoken) {
                    historyItemData.setTimetoken(historyItem.get("timetoken").asLong());
                    message = processMessage(historyItem.get("message"));
                } else {
                    message = processMessage(historyItem);
                }

                historyItemData.setEntry(message);
                historyData.getMessages().add(historyItemData);
            }
        }

        return historyData;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNHistoryOperation;
    }

    private Object processMessage(JsonNode message) throws PubnubException {
        if (this.pubnub.getConfiguration().getCipherKey() == null) {
            return message;
        }

        Crypto crypto = new Crypto(pubnub.getConfiguration().getCipherKey());
        String outputText = crypto.decrypt(message.asText());

        ObjectMapper mapper = new ObjectMapper();
        Object outputObject;
        try {
            outputObject = mapper.readValue(outputText, JsonNode.class);
        } catch (IOException e) {
            throw new PubnubException(PubnubError.PNERROBJ_PARSING_ERROR);
        }

        return outputObject;
    }

}