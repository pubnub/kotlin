package com.pubnub.api.endpoints.push;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import lombok.Singular;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class ModifyProvisions extends Endpoint<List<Object>, Boolean> {

    private Pubnub pubnub;
    private PushType pushType;
    private @Singular List<String> addChannels;
    private @Singular List<String> removeChannels;
    private String deviceId;
    private boolean removeAllChannels;

    @Override
    protected boolean validateParams() {
        if (pushType == null) {
            return false;
        }

        if (deviceId == null || deviceId.length() == 0) {
            return false;
        }

        return true;
    }

    public ModifyProvisions removeAllChannels() {
        removeAllChannels = true;
        return this;
    }

    @Override
    protected Call<List<Object>> doWork() throws PubnubException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("type", pushType.name().toLowerCase());

        if (addChannels.size() != 0) {
            params.put("add", PubnubUtil.joinString(addChannels, ","));
        }

        if (removeChannels.size() != 0) {
            params.put("remove", PubnubUtil.joinString(removeChannels, ","));
        }

        PushService service = this.createRetrofit(pubnub).create(PushService.class);

        if (this.removeAllChannels) {
            return service.removeAllChannelsForDevice(pubnub.getConfiguration().getSubscribeKey(), deviceId, params);
        } else {
            return service.modifyChannelsForDevice(pubnub.getConfiguration().getSubscribeKey(), deviceId, params);
        }


    }

    @Override
    protected Boolean createResponse(Response<List<Object>> input) throws PubnubException {
        return true;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNPushNotificationModifiedChannelsOperations;
    }

}
