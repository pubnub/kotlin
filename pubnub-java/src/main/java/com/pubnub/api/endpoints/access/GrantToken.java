package com.pubnub.api.endpoints.access;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.models.server.access_manager.v3.GrantTokenRequestBody;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.pubnub.api.PubNubUtil.isNullOrEmpty;

@Accessors(chain = true, fluent = true)
public class GrantToken extends Endpoint<JsonObject, PNGrantTokenResult> {

    @Setter
    private Integer ttl;
    @Setter
    private Object meta;
    @Setter
    private String authorizedUUID;
    @Setter
    private List<ChannelGrant> channels = Collections.emptyList();
    @Setter
    private List<ChannelGroupGrant> channelGroups = Collections.emptyList();
    @Setter
    private List<UUIDGrant> uuids = Collections.emptyList();

    public GrantToken(PubNub pubnub,
                      TelemetryManager telemetryManager,
                      RetrofitManager retrofit,
                      TokenManager tokenManager) {
        super(pubnub, telemetryManager, retrofit, tokenManager);
    }

    @Override
    protected List<String> getAffectedChannels() {
        final ArrayList<String> affectedChannels = new ArrayList<>();
        for (ChannelGrant channelGrant : channels) {
            affectedChannels.add(channelGrant.getId());
        }
        return affectedChannels;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        final ArrayList<String> affectedChannelGroups = new ArrayList<>();
        for (ChannelGroupGrant channelGroupGrant : channelGroups) {
            affectedChannelGroups.add(channelGroupGrant.getId());
        }
        return affectedChannelGroups;
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.getPubnub().getConfiguration().getSecretKey() == null || this.getPubnub()
                .getConfiguration()
                .getSecretKey()
                .isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SECRET_KEY_MISSING).build();
        }
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null || this.getPubnub()
                .getConfiguration()
                .getSubscribeKey()
                .isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (isNullOrEmpty(channels)
                && isNullOrEmpty(channelGroups)
                && isNullOrEmpty(uuids)) {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_RESOURCES_MISSING)
                    .build();
        }
        if (this.ttl == null) {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_TTL_MISSING)
                    .build();
        }
    }

    @Override
    protected Call<JsonObject> doWork(Map<String, String> queryParams) throws PubNubException {
        GrantTokenRequestBody requestBody = GrantTokenRequestBody.builder()
                .ttl(ttl)
                .channels(channels)
                .groups(channelGroups)
                .uuids(uuids)
                .meta(meta)
                .uuid(authorizedUUID)
                .build();

        return this.getRetrofit()
                .getAccessManagerService()
                .grantToken(this.getPubnub().getConfiguration().getSubscribeKey(), requestBody, queryParams);
    }

    @Override
    protected PNGrantTokenResult createResponse(Response<JsonObject> input) throws PubNubException {
        if (input.body() == null) {
            return null;
        }

        return new PNGrantTokenResult(input.body().getAsJsonObject("data").get("token").getAsString());
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNAccessManagerGrantToken;
    }

    @Override
    protected boolean isAuthRequired() {
        return false;
    }
}
