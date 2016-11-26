package com.pubnub.api.endpoints.presence;


import com.fasterxml.jackson.databind.JsonNode;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.server.Envelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class HereNow extends Endpoint<Envelope<JsonNode>, PNHereNowResult> {
    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private Boolean includeState;
    @Setter
    private Boolean includeUUIDs;

    public HereNow(PubNub pubnubInstance, Retrofit retrofit) {
        super(pubnubInstance, retrofit);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }


    @Override
    protected void validateParams() throws PubNubException {
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null || this.getPubnub().getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
    }

    @Override
    protected Call<Envelope<JsonNode>> doWork(Map<String, String> params) {

        if (includeState == null) {
            includeState = false;
        }

        if (includeUUIDs == null) {
            includeUUIDs = true;
        }

        String channelCSV;

        PresenceService service = this.getRetrofit().create(PresenceService.class);

        if (includeState) {
            params.put("state", "1");
        }
        if (!includeUUIDs) {
            params.put("disable_uuids", "1");
        }
        if (channelGroups.size() > 0) {
            params.put("channel-group", PubNubUtil.joinString(channelGroups, ","));
        }

        if (channels.size() > 0) {
            channelCSV = PubNubUtil.joinString(channels, ",");
        } else {
            channelCSV = ",";
        }

        if (channels.size() > 0 || channelGroups.size() > 0) {
            return service.hereNow(this.getPubnub().getConfiguration().getSubscribeKey(), channelCSV, params);
        } else {
            return service.globalHereNow(this.getPubnub().getConfiguration().getSubscribeKey(), params);
        }
    }

    @Override
    protected PNHereNowResult createResponse(Response<Envelope<JsonNode>> input) {
        PNHereNowResult herenowData;

        if (channels.size() > 1 || channelGroups.size() > 0) {
            herenowData = parseMultipleChannelResponse(input.body().getPayload());
        } else {
            herenowData = parseSingleChannelResponse(input.body());
        }

        return herenowData;
    }

    private PNHereNowResult parseSingleChannelResponse(Envelope<JsonNode> input) {
        PNHereNowResult hereNowData = PNHereNowResult.builder()
                .totalChannels(1)
                .channels(new HashMap<String, PNHereNowChannelData>())
                .totalOccupancy(input.getOccupancy())
                .build();

        PNHereNowChannelData.PNHereNowChannelDataBuilder hereNowChannelData = PNHereNowChannelData.builder()
                .channelName(channels.get(0))
                .occupancy(input.getOccupancy());

        if (includeUUIDs) {
            hereNowChannelData.occupants(prepareOccupantData(input.getUuids()));
            hereNowData.getChannels().put(channels.get(0), hereNowChannelData.build());
        }

        return hereNowData;
    }

    private PNHereNowResult parseMultipleChannelResponse(JsonNode input) {
        PNHereNowResult hereNowData = PNHereNowResult.builder()
                .channels(new HashMap<String, PNHereNowChannelData>())
                .totalChannels(input.get("total_channels").asInt())
                .totalOccupancy(input.get("total_occupancy").asInt())
                .build();

        for (Iterator<Map.Entry<String, JsonNode>> it = input.get("channels").fields(); it.hasNext();) {
            Map.Entry<String, JsonNode> entry = it.next();

            PNHereNowChannelData.PNHereNowChannelDataBuilder hereNowChannelData = PNHereNowChannelData.builder()
                    .channelName(entry.getKey())
                    .occupancy(entry.getValue().get("occupancy").asInt());

            if (includeUUIDs) {
                hereNowChannelData.occupants(prepareOccupantData(entry.getValue().get("uuids")));
            } else {
                hereNowChannelData.occupants(null);
            }

            hereNowData.getChannels().put(entry.getKey(), hereNowChannelData.build());
        }

        return hereNowData;
    }

    private List<PNHereNowOccupantData> prepareOccupantData(JsonNode input) {
        List<PNHereNowOccupantData> occupantsResults = new ArrayList<>();

        if (includeState != null && includeState) {
            for (Iterator<JsonNode> it = input.elements(); it.hasNext();) {
                JsonNode occupant = it.next();
                PNHereNowOccupantData.PNHereNowOccupantDataBuilder hereNowOccupantData = PNHereNowOccupantData.builder();
                hereNowOccupantData.uuid(occupant.get("uuid").asText());
                hereNowOccupantData.state(occupant.get("state"));

                occupantsResults.add(hereNowOccupantData.build());
            }
        } else {
            for (Iterator<JsonNode> it = input.elements(); it.hasNext();) {
                JsonNode occupant = it.next();
                PNHereNowOccupantData.PNHereNowOccupantDataBuilder hereNowOccupantData = PNHereNowOccupantData.builder();
                hereNowOccupantData.uuid(occupant.asText());
                hereNowOccupantData.state(null);

                occupantsResults.add(hereNowOccupantData.build());
            }
        }

        return occupantsResults;
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNHereNowOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

}
