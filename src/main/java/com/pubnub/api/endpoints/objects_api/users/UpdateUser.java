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
import com.pubnub.api.models.consumer.objects_api.user.PNUpdateUserResult;
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
public class UpdateUser extends Endpoint<EntityEnvelope<PNUser>, PNUpdateUserResult>
        implements InclusionParamsProvider<UpdateUser, PNUserFields>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;

    @Setter
    private PNUser user;

    public UpdateUser(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
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

        if (this.user == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_USER_MISSING).build();
        }

        if (this.user.getId() == null || this.user.getId().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_USER_ID_MISSING).build();
        }

        // todo validate user custom json
    }

    @Override
    protected Call<EntityEnvelope<PNUser>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getUserService()
                .updateUser(this.getPubnub().getConfiguration().getSubscribeKey(), user.getId(), user, params);
    }

    @Override
    protected PNUpdateUserResult createResponse(Response<EntityEnvelope<PNUser>> input) throws PubNubException {
        PNUpdateUserResult.PNUpdateUserResultBuilder resultBuilder = PNUpdateUserResult.builder();

        if (input.body() != null) {
            resultBuilder.user(input.body().getData());
        }
        return resultBuilder.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNUpdateUserOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public UpdateUser includeFields(PNUserFields... params) {
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
