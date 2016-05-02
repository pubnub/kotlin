package com.pubnub.api.endpoints.push;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class AddChannelsToPush extends Endpoint<List<Object>, PNPushAddChannelResult> {

    @Setter private PNPushType pushType;
    @Setter private List<String> channels;
    @Setter String deviceId;

    public AddChannelsToPush(PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (pubnub.getConfiguration().getSubscribeKey()==null || pubnub.getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (pushType == null) {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_PUSH_TYPE_MISSING).build();
        }
        if (deviceId == null || deviceId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_DEVICE_ID_MISSING).build();
        }
        if (channels.size() == 0) {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_CHANNEL_MISSING).build();
        }
    }

    @Override
    protected Call<List<Object>> doWork(Map<String, String> baseParams) throws PubNubException {
        baseParams.put("type", pushType.name().toLowerCase());

        if (channels.size() != 0) {
            baseParams.put("add", PubNubUtil.joinString(channels, ","));
        }

        PushService service = this.createRetrofit().create(PushService.class);
        return service.modifyChannelsForDevice(pubnub.getConfiguration().getSubscribeKey(), deviceId, baseParams);

    }

    @Override
    protected PNPushAddChannelResult createResponse(Response<List<Object>> input) throws PubNubException {
        if (input.body() == null) {
            throw PubNubException.builder().pubnubError(PubNubError.PNERROBJ_PARSING_ERROR).build();
        }

        return PNPushAddChannelResult.builder().build();
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNPushNotificationEnabledChannelsOperation;
    }
}
