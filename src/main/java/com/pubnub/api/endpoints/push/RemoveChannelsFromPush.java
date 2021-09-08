package com.pubnub.api.endpoints.push;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;

import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

@Accessors(chain = true, fluent = true)
public class RemoveChannelsFromPush extends Endpoint<List<Object>, PNPushRemoveChannelResult> {

    @Setter
    private PNPushType pushType;
    @Setter
    private List<String> channels;
    @Setter
    private String deviceId;
    @Setter
    private PNPushEnvironment environment;
    @Setter
    private String topic;

    public RemoveChannelsFromPush(PubNub pubnub,
                                  TelemetryManager telemetryManager,
                                  RetrofitManager retrofit,
                                  TokenManager tokenManager) {
        super(pubnub, telemetryManager, retrofit, tokenManager);
    }

    @Override
    protected List<String> getAffectedChannels() {
        return channels;
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
        if (channels == null || channels.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING).build();
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
    protected Call<List<Object>> doWork(Map<String, String> baseParams) throws PubNubException {
        baseParams.put("remove", PubNubUtil.joinString(channels, ","));

        if (pushType != PNPushType.APNS2) {
            baseParams.put("type", pushType.toString());
            return this.getRetrofit().getPushService().modifyChannelsForDevice(
                    this.getPubnub().getConfiguration().getSubscribeKey(),
                    deviceId,
                    baseParams);
        } else {
            baseParams.put("environment", environment.name().toLowerCase());
            baseParams.put("topic", topic);

            return this.getRetrofit().getPushService().modifyChannelsForDeviceApns2(
                    this.getPubnub().getConfiguration().getSubscribeKey(),
                    deviceId,
                    baseParams);
        }
    }

    @Override
    protected PNPushRemoveChannelResult createResponse(Response<List<Object>> input) throws PubNubException {
        if (input.body() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }
        return PNPushRemoveChannelResult.builder().build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNRemovePushNotificationsFromChannelsOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }
}
