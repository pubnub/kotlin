package com.pubnub.api.endpoints.push;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;

import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

@Accessors(chain = true, fluent = true)
public class ListPushProvisions extends Endpoint<List<String>, PNPushListProvisionsResult> {

    @Setter
    private PNPushType pushType;
    @Setter
    private String deviceId;
    @Setter
    private PNPushEnvironment environment;
    @Setter
    private String topic;

    public ListPushProvisions(PubNub pubnub,
                              TelemetryManager telemetryManager,
                              RetrofitManager retrofit,
                              TokenManager tokenManager) {
        super(pubnub, telemetryManager, retrofit, tokenManager);
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
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null
                || this.getPubnub().getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (pushType == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PUSH_TYPE_MISSING).build();
        }
        if (deviceId == null || deviceId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_DEVICE_ID_MISSING).build();
        }
        if (pushType == PNPushType.APNS2) {
            if (topic == null || topic.isEmpty()) {
                throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PUSH_TOPIC_MISSING).build();
            }
            if (environment == null) {
                environment = PNPushEnvironment.DEVELOPMENT;
            }
        }
    }

    @Override
    protected Call<List<String>> doWork(Map<String, String> params) throws PubNubException {
        if (pushType != PNPushType.APNS2) {
            params.put("type", pushType.toString());
            return this.getRetrofit().getPushService().listChannelsForDevice(
                    this.getPubnub().getConfiguration().getSubscribeKey(), deviceId, params);
        } else {
            params.put("environment", environment.name().toLowerCase());
            params.put("topic", topic);
            return this.getRetrofit().getPushService().listChannelsForDeviceApns2(
                    this.getPubnub().getConfiguration().getSubscribeKey(),
                    deviceId,
                    params);
        }
    }

    @Override
    protected PNPushListProvisionsResult createResponse(Response<List<String>> input) throws PubNubException {
        if (input.body() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        return PNPushListProvisionsResult.builder().channels(input.body()).build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNPushNotificationEnabledChannelsOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

}
