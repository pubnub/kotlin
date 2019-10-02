package com.pubnub.api.endpoints.objects_api.users;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNUserFields;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.PNResourceType;
import com.pubnub.api.managers.token_manager.TokenManagerProperties;
import com.pubnub.api.managers.token_manager.TokenManagerPropertyProvider;
import com.pubnub.api.models.consumer.objects_api.user.PNGetUserResult;
import com.pubnub.api.models.consumer.objects_api.user.PNUser;
import com.pubnub.api.models.consumer.objects_api.util.InclusionParamsProvider;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class GetUser extends Endpoint<EntityEnvelope<PNUser>, PNGetUserResult>
        implements InclusionParamsProvider<GetUser, PNUserFields>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;

    @Setter
    private String userId;

    public GetUser(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
        super(pubnubInstance, telemetry, retrofitInstance);
        extraParamsMap = new HashMap<>();
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

        if (this.userId == null || this.userId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_USER_ID_MISSING).build();
        }
    }

    @Override
    protected Call<EntityEnvelope<PNUser>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getUserService()
                .getUser(this.getPubnub().getConfiguration().getSubscribeKey(), userId, params);
    }

    @Override
    protected PNGetUserResult createResponse(Response<EntityEnvelope<PNUser>> input) throws PubNubException {
        PNGetUserResult.PNGetUserResultBuilder resultBuilder = PNGetUserResult.builder();

        if (input.body() != null) {
            resultBuilder.user(input.body().getData());
        }
        return resultBuilder.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetUserOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public GetUser includeFields(PNUserFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public TokenManagerProperties getTmsProperties() {
        return TokenManagerProperties.builder()
                .pnResourceType(PNResourceType.USER)
                .resourceId(userId)
                .build();
    }
}
