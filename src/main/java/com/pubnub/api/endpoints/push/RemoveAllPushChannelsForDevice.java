package com.pubnub.api.endpoints.push;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class RemoveAllPushChannelsForDevice extends Endpoint<List<Object>, PNPushRemoveAllChannelsResult> {

    @Setter
    private PNPushType pushType;
    @Setter
    private String deviceId;

    public RemoveAllPushChannelsForDevice(PubNub pubnub, Retrofit retrofit) {
        super(pubnub, retrofit);
    }

    @Override
    protected List<String> getAffectedChannels() {
        return null;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return null;
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null || this.getPubnub().getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (pushType == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PUSH_TYPE_MISSING).build();
        }
        if (deviceId == null || deviceId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_DEVICE_ID_MISSING).build();
        }
    }


    @Override
    protected Call<List<Object>> doWork(Map<String, String> params) throws PubNubException {
        params.put("type", pushType.name().toLowerCase());

        PushService service = this.getRetrofit().create(PushService.class);

        return service.removeAllChannelsForDevice(this.getPubnub().getConfiguration().getSubscribeKey(), deviceId, params);

    }

    @Override
    protected PNPushRemoveAllChannelsResult createResponse(Response<List<Object>> input) throws PubNubException {
        if (input.body() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        return PNPushRemoveAllChannelsResult.builder().build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return null; // PNOperationType.PNPushNotificationModifiedChannelsOperations;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

}
