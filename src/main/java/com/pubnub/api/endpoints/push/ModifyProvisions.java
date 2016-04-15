package com.pubnub.api.endpoints.push;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class ModifyProvisions extends Endpoint<List<Object>, Boolean> {

    @Setter private PushType pushType;
    @Setter private @Singular List<String> addChannels;
    @Setter private @Singular List<String> removeChannels;
    @Setter String deviceId;
    @Setter private boolean removeAllChannels;

    public ModifyProvisions(Pubnub pubnub) {
        super(pubnub);
        addChannels = new ArrayList<>();
        removeChannels = new ArrayList<>();
    }

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
    protected Call<List<Object>> doWork(Map<String, String> params) throws PubnubException {
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
