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
import com.pubnub.api.models.consumer.history.PNFetchMessageItem;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.api.models.server.FetchMessagesEnvelope;
import com.pubnub.api.vendor.Crypto;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS;

@Slf4j
@Accessors(chain = true, fluent = true)
public class FetchMessages extends Endpoint<FetchMessagesEnvelope, PNFetchMessagesResult> {
    private static final int DEFAULT_MESSAGES = 1;
    private static final int MAX_MESSAGES = 25;
    private static final int MAX_MESSAGES_ACTIONS = 100;

    @Setter
    private List<String> channels;
    @Setter
    private Integer maximumPerChannel;
    @Setter
    private Long start;
    @Setter
    private Long end;

    @Setter
    private Boolean includeMeta;
    @Setter
    private Boolean includeMessageActions;

    public FetchMessages(PubNub pubnub, TelemetryManager telemetryManager, RetrofitManager retrofit) {
        super(pubnub, telemetryManager, retrofit);
        channels = new ArrayList<>();
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
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null
                || this.getPubnub().getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }

        if (channels == null || channels.size() == 0) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING).build();
        }

        if (includeMeta == null) {
            includeMeta = false;
        }

        if (includeMessageActions == null) {
            includeMessageActions = false;
        }

        if (!includeMessageActions) {
            if (maximumPerChannel == null || maximumPerChannel < DEFAULT_MESSAGES) {
                maximumPerChannel = DEFAULT_MESSAGES;
                log.info("maximumPerChannel param defaulting to " + maximumPerChannel);
            } else if (maximumPerChannel > MAX_MESSAGES) {
                maximumPerChannel = MAX_MESSAGES;
                log.info("maximumPerChannel param defaulting to " + maximumPerChannel);
            }
        } else {
            if (maximumPerChannel == null || maximumPerChannel < 1 || maximumPerChannel > MAX_MESSAGES_ACTIONS) {
                maximumPerChannel = MAX_MESSAGES_ACTIONS;
                log.info("maximumPerChannel param defaulting to " + maximumPerChannel);
            }
        }
    }

    @Override
    protected Call<FetchMessagesEnvelope> doWork(Map<String, String> params) throws PubNubException {
        params.put("max", String.valueOf(maximumPerChannel));

        if (start != null) {
            params.put("start", Long.toString(start).toLowerCase());
        }
        if (end != null) {
            params.put("end", Long.toString(end).toLowerCase());
        }

        if (includeMeta) {
            params.put("include_meta", String.valueOf(includeMeta));
        }

        if (!includeMessageActions) {
            return this.getRetrofit().getHistoryService().fetchMessages(
                    this.getPubnub().getConfiguration().getSubscribeKey(), PubNubUtil.joinString(channels, ","),
                    params);
        } else {
            if (channels.size() > 1) {
                throw PubNubException.builder().pubnubError(PNERROBJ_HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS).build();
            }
            return this.getRetrofit().getHistoryService().fetchMessagesWithActions(
                    this.getPubnub().getConfiguration().getSubscribeKey(), channels.get(0), params);
        }
    }

    @Override
    protected PNFetchMessagesResult createResponse(Response<FetchMessagesEnvelope> input) throws PubNubException {
        if (input.body() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        PNFetchMessagesResult.PNFetchMessagesResultBuilder builder = PNFetchMessagesResult.builder();

        HashMap<String, List<PNFetchMessageItem>> channelsMap = new HashMap<>();

        for (Map.Entry<String, List<PNFetchMessageItem>> entry : input.body().getChannels().entrySet()) {
            List<PNFetchMessageItem> items = new ArrayList<>();

            for (PNFetchMessageItem item : entry.getValue()) {
                PNFetchMessageItem.PNFetchMessageItemBuilder messageItemBuilder = PNFetchMessageItem.builder();
                messageItemBuilder.message(processMessage(item.getMessage()));
                messageItemBuilder.timetoken(item.getTimetoken());
                messageItemBuilder.meta(item.getMeta());

                if (includeMessageActions) {
                    if (item.getActions() != null) {
                        messageItemBuilder.actions(item.getActions());
                    } else {
                        messageItemBuilder.actions(new HashMap<>());
                    }
                }

                items.add(messageItemBuilder.build());
            }

            channelsMap.put(entry.getKey(), items);
        }

        builder.channels(channelsMap);

        return builder.build();
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

        // inject the decoded response into the payload
        if (mapper.isJsonObject(message) && mapper.hasField(message, "pn_other")) {
            JsonObject objectNode = mapper.getAsObject(message);
            mapper.putOnObject(objectNode, "pn_other", outputObject);
            outputObject = objectNode;
        }

        return outputObject;
    }
}
