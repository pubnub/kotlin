package com.pubnub.api.endpoints.presence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pubnub.api.core.*;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.consumer_facing.PNSetStateResult;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.managers.SubscriptionManager;
import lombok.*;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Accessors(chain = true, fluent = true)
public class SetState extends Endpoint<Envelope<Map<String, Object>>, PNSetStateResult> {

    @Getter(AccessLevel.NONE)
    private SubscriptionManager subscriptionManager;

    @Setter private List<String> channels;
    @Setter private List<String> channelGroups;
    @Setter private Object state;

    public SetState(Pubnub pubnub, SubscriptionManager subscriptionManager) {
        super(pubnub);
        this.subscriptionManager = subscriptionManager;
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected boolean validateParams() {

        if (state == null) {
            return false;
        }

        if (channels.size() == 0 && channelGroups.size() == 0) {
            return false;
        }


        return true;
    }

    @Override
    protected Call<Envelope<Map<String, Object>>> doWork(Map<String, String> params) throws PubnubException {
        ObjectWriter ow = new ObjectMapper().writer();
        String stringifiedState;


        subscriptionManager.adaptStateBuilder(channels, channelGroups, state);

        PresenceService service = this.createRetrofit().create(PresenceService.class);

        if (channelGroups.size() > 0) {
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        try {
            stringifiedState = ow.writeValueAsString(state);
        } catch (JsonProcessingException e) {
            throw PubnubException.builder().pubnubError(PubnubError.PNERROBJ_INVALID_ARGUMENTS).errormsg(e.getMessage()).build();
        }

        stringifiedState = PubnubUtil.urlEncode(stringifiedState);
        params.put("state", stringifiedState);

        String channelCSV = channels.size() > 0 ? PubnubUtil.joinString(channels, ",") : ",";

        return service.setState(pubnub.getConfiguration().getSubscribeKey(), channelCSV, this.pubnub.getConfiguration().getUuid(), params);
    }

    @Override
    protected PNSetStateResult createResponse(Response<Envelope<Map<String, Object>>> input) throws PubnubException {
        PnResponse<Boolean> pnResponse = new PnResponse<Boolean>();
        pnResponse.fillFromRetrofit(input);

        if (input.body() == null) {
            throw PubnubException.builder().pubnubError(PubnubError.PNERROBJ_PARSING_ERROR).build();
        }

        PNSetStateResult pnSetStateResult = new PNSetStateResult();
        pnSetStateResult.setState(input.body().getPayload());

        return pnSetStateResult;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNSetStateOperation;
    }

}
