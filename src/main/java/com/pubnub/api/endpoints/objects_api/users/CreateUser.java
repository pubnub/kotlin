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
import com.pubnub.api.models.consumer.objects_api.user.PNCreateUserResult;
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
public class CreateUser extends Endpoint<EntityEnvelope<PNUser>, PNCreateUserResult>
        implements InclusionParamsProvider<CreateUser, PNUserFields>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;

    @Setter
    private PNUser user;

    public CreateUser(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
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
        if (user == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_USER_MISSING).build();
        }
        if (user.getId() == null || user.getId().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_USER_ID_MISSING).build();
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_USER_NAME_MISSING).build();
        }
    }

    @Override
    protected Call<EntityEnvelope<PNUser>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getUserService()
                .createUser(this.getPubnub().getConfiguration().getSubscribeKey(), user, params);
    }

    @Override
    protected PNCreateUserResult createResponse(Response<EntityEnvelope<PNUser>> input) throws PubNubException {
        PNCreateUserResult.PNCreateUserResultBuilder resultBuilder = PNCreateUserResult.builder();

        if (input.body() != null) {
            resultBuilder.user(input.body().getData());
        }

        return resultBuilder.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNCreateUserOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public CreateUser includeFields(PNUserFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public TokenManagerProperties getTmsProperties() {
        return TokenManagerProperties.builder()
                .pnResourceType(PNResourceType.USER)
                .resourceId(user.getId())
                .build();
    }
}
