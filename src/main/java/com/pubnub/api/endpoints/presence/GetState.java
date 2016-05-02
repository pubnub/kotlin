package com.pubnub.api.endpoints.presence;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubnubUtil;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.pubnub.api.models.server.Envelope;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class GetState extends Endpoint<Envelope<Object>, PNGetStateResult> {

    @Setter private List<String> channels;
    @Setter private List<String> channelGroups;
    @Setter private String uuid;

    public GetState(PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (pubnub.getConfiguration().getSubscribeKey()==null || pubnub.getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (channels.size()==0 && channelGroups.size()==0)
        {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_CHANNEL_AND_GROUP_MISSING).build();
        }
    }

    @Override
    protected Call<Envelope<Object>> doWork(Map<String, String> params) {
        PresenceService service = this.createRetrofit().create(PresenceService.class);

        if (channelGroups.size() > 0) {
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        String channelCSV;

        if (channels.size() > 0) {
            channelCSV = PubnubUtil.joinString(channels, ",");
        }
        else
        {
            channelCSV = ",";
        }

        String selectedUUID = uuid != null ? uuid : pubnub.getConfiguration().getUuid();

        return service.getState(pubnub.getConfiguration().getSubscribeKey(), channelCSV, selectedUUID, params);
    }

    @Override
    protected PNGetStateResult createResponse(final Response<Envelope<Object>> input) throws PubNubException {
        Map<String, Object> stateMappings;

        if (channels.size() == 1 && channelGroups.size() == 0) {
            stateMappings = new HashMap<>();
            stateMappings.put(channels.get(0), input.body().getPayload());
        } else {
            stateMappings = (Map<String, Object>) input.body().getPayload();
        }

        return PNGetStateResult.builder().stateByUUID(stateMappings).build();
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetState;
    }

}
