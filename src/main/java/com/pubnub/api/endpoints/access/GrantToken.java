package com.pubnub.api.endpoints.access;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.Space;
import com.pubnub.api.models.consumer.access_manager.v3.User;
import com.pubnub.api.models.server.access_manager.v3.GrantTokenRequestBody;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class GrantToken extends Endpoint<JsonObject, PNGrantTokenResult> {

    @Setter
    private Integer ttl;

    @Setter
    private Object meta;

    private List<User> userList;
    private List<Space> spaceList;

    public GrantToken(PubNub pubnub, TelemetryManager telemetryManager, RetrofitManager retrofit) {
        super(pubnub, telemetryManager, retrofit);
        userList = new ArrayList<>();
        spaceList = new ArrayList<>();
    }

    @Override
    protected List<String> getAffectedChannels() {
        // generate a list of channels when they become supported in PAMv3
        return null;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        // generate a list of groups when they become supported in PAMv3
        return null;
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
        if ((this.userList == null || this.userList.isEmpty())
                && (this.spaceList == null || this.spaceList.isEmpty())) {
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
        JsonObject requestBody = GrantTokenRequestBody.builder()
                .pubNub(this.getPubnub())
                .ttl(ttl)
                .spaces(spaceList)
                .users(userList)
                .meta(meta)
                .build()
                .assemble();

        return this.getRetrofit()
                .getAccessManagerService()
                .grantToken(this.getPubnub().getConfiguration().getSubscribeKey(), requestBody, queryParams);
    }

    @Override
    protected PNGrantTokenResult createResponse(Response<JsonObject> input) throws PubNubException {
        PNGrantTokenResult.PNGrantTokenResultBuilder builder = PNGrantTokenResult.builder();

        if (input.body() != null) {
            builder.token(input.body().getAsJsonObject("data").get("token").getAsString());
        }

        return builder.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNAccessManagerGrantToken;
    }

    @Override
    protected boolean isAuthRequired() {
        return false;
    }

    public GrantToken users(User... users) {
        this.userList.clear();
        this.userList.addAll(Arrays.asList(users));
        return this;
    }

    public GrantToken spaces(Space... spaces) {
        this.spaceList.clear();
        this.spaceList.addAll(Arrays.asList(spaces));
        return this;
    }
}
