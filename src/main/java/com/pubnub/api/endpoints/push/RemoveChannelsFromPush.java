package com.pubnub.api.endpoints.push;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.enums.PushType;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class RemoveChannelsFromPush extends Endpoint<List<Object>, Boolean> {

    @Setter private PushType pushType;
    @Setter private List<String> channels;
    @Setter String deviceId;

    public RemoveChannelsFromPush(Pubnub pubnub) {
        super(pubnub);

        channels = new ArrayList<>();
    }

    @Override
    protected boolean validateParams() {
        if (pushType == null) {
            return false;
        }

        if (deviceId == null || deviceId.length() == 0) {
            return false;
        }

        if (channels.size() == 0) {
            return false;
        }

        return true;
    }

    @Override
    protected Call<List<Object>> doWork(Map<String, String> baseParams) throws PubnubException {
        baseParams.put("type", pushType.name().toLowerCase());

        if (channels.size() != 0) {
            baseParams.put("remove", PubnubUtil.joinString(channels, ","));
        }

        PushService service = this.createRetrofit().create(PushService.class);
        return service.modifyChannelsForDevice(pubnub.getConfiguration().getSubscribeKey(), deviceId, baseParams);

    }

    @Override
    protected Boolean createResponse(Response<List<Object>> input) throws PubnubException {
        return null;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNRemovePushNotificationsFromChannelsOperation;
    }
}
