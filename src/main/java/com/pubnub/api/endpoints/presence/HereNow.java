package com.pubnub.api.endpoints.presence;


import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.HereNow.HereNowChannelData;
import com.pubnub.api.core.models.HereNow.HereNowData;
import com.pubnub.api.core.models.HereNow.HereNowOccupantData;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import lombok.Singular;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class HereNow extends Endpoint<Envelope<Object>, HereNowData> {
    private Pubnub pubnub;
    @Singular private List<String> channels;
    @Singular private List<String> channelGroups;
    private Boolean includeState;
    private Boolean includeUUIDs;


    @Override
    protected boolean validateParams() {
        return true;
    }

    @Override
    protected Call<Envelope<Object>> doWork() {

        if (includeState == null) {
            includeState = false;
        }

        if (includeUUIDs == null) {
            includeUUIDs = true;
        }

        String channelCSV;
        Map<String, String> params = new HashMap<String, String>();

        PresenceService service = this.createRetrofit(pubnub).create(PresenceService.class);

        if (includeState) {
            params.put("state", "1");
        }
        if (!includeUUIDs) {
            params.put("disable_uuids", "1");
        }
        if (channelGroups.size() > 0) {
            params.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        if (channels.size() > 0) {
            channelCSV = PubnubUtil.joinString(channels, ",");
        } else {
            channelCSV = ",";
        }

        if (channels.size() > 0 || channelGroups.size() > 0) {
            return service.hereNow(pubnub.getConfiguration().getSubscribeKey(), channelCSV, params);
        } else {
            return service.globalHereNow(pubnub.getConfiguration().getSubscribeKey(), params);
        }
    }

    @Override
    protected HereNowData createResponse(Response<Envelope<Object>> input) {
        PnResponse<HereNowData> pnResponse = new PnResponse<HereNowData>();
        HereNowData herenowData;

        if (channels.size() > 1 || channelGroups.size() > 0) {
            herenowData = parseMultipleChannelResponse(input.body().getPayload());
        } else {
            herenowData = parseSingleChannelResponse(input.body());
        }

        return herenowData;
    }

    private HereNowData parseSingleChannelResponse(Envelope<Object> input) {
        HereNowData hereNowData = new HereNowData();
        hereNowData.setTotalChannels(1);
        hereNowData.setTotalOccupancy(input.getOccupancy());

        HereNowChannelData hereNowChannelData = new HereNowChannelData();
        hereNowChannelData.setChannelName(channels.get(0));
        hereNowChannelData.setOccupancy(input.getOccupancy());

        if (includeUUIDs) {
            hereNowChannelData.setOccupants(prepareOccupantData(input.getUuids()));
            hereNowData.getChannels().put(channels.get(0), hereNowChannelData);
        }

        return hereNowData;
    }

    private HereNowData parseMultipleChannelResponse(Object input) {
        HereNowData hereNowData = new HereNowData();
        Map<String, Object> parsedInput = (Map<String, Object>) input;

        hereNowData.setTotalChannels((Integer) parsedInput.get("total_channels"));
        hereNowData.setTotalOccupancy((Integer) parsedInput.get("total_occupancy"));

        Map<String, Object> channels = (HashMap<String, Object>) parsedInput.get("channels");

        for (String channelName: channels.keySet()) {
            Map<String, Object> channel = (Map<String, Object>) channels.get(channelName);

            HereNowChannelData hereNowChannelData = new HereNowChannelData();
            hereNowChannelData.setChannelName(channelName);
            hereNowChannelData.setOccupancy((Integer) channel.get("occupancy"));

            if (includeUUIDs) {
                hereNowChannelData.setOccupants(prepareOccupantData(channel.get("uuids")));
            } else {
                hereNowChannelData.setOccupants(null);
            }

            hereNowData.getChannels().put(channelName, hereNowChannelData);
        }

        return hereNowData;
    }

    private List<HereNowOccupantData> prepareOccupantData(Object input) {
        List<HereNowOccupantData> occupantsResults = new ArrayList<HereNowOccupantData>();

        if (includeState != null && includeState) {
            for (Map<String, Object> occupant : (List<Map<String, Object>>) input) {
                HereNowOccupantData hereNowOccupantData = new HereNowOccupantData();
                hereNowOccupantData.setUuid((String) occupant.get("uuid"));
                hereNowOccupantData.setState(occupant.get("state"));

                occupantsResults.add(hereNowOccupantData);
            }
        } else {
            for (String occupant : (List<String>) input) {
                HereNowOccupantData hereNowOccupantData = new HereNowOccupantData();
                hereNowOccupantData.setUuid(occupant);
                hereNowOccupantData.setState(null);

                occupantsResults.add(hereNowOccupantData);
            }
        }
        return occupantsResults;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNHereNowOperation;
    }

}
