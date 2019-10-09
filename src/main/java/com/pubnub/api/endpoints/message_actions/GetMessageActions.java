package com.pubnub.api.endpoints.message_actions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.MapperManager;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class GetMessageActions extends Endpoint<JsonObject, PNGetMessageActionsResult> {

    @Setter
    private String channel;

    @Setter
    private Long start;

    @Setter
    private Long end;

    @Setter
    private Integer limit;

    public GetMessageActions(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
        super(pubnubInstance, telemetry, retrofitInstance);
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
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null || this.getPubnub()
                .getConfiguration()
                .getSubscribeKey()
                .isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
    }

    @Override
    protected Call<JsonObject> doWork(Map<String, String> params) {

        if (start != null) {
            params.put("start", Long.toString(start).toLowerCase());
        }

        if (end != null) {
            params.put("end", Long.toString(end).toLowerCase());
        }

        if (limit != null) {
            params.put("limit", String.valueOf(limit));
        }

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getMessageActionService()
                .getMessageActions(this.getPubnub().getConfiguration().getSubscribeKey(), channel, params);
    }

    @Override
    protected PNGetMessageActionsResult createResponse(Response<JsonObject> input) throws PubNubException {
        PNGetMessageActionsResult.PNGetMessageActionsResultBuilder builder = PNGetMessageActionsResult.builder();

        if (input.body() != null) {
            MapperManager mapper = getPubnub().getMapper();
            List<PNMessageAction> pnMessageActionList = new ArrayList<>();

            Iterator<JsonElement> it = mapper.getArrayIterator(input.body(), "data");
            while (it.hasNext()) {
                JsonElement messageActionJson = it.next();
                pnMessageActionList.add(mapper.convertValue(messageActionJson, PNMessageAction.class));
            }

            builder.actions(pnMessageActionList);
        }

        return builder.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetMessageActions;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }
}
