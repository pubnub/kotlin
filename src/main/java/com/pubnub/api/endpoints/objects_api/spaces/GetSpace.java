package com.pubnub.api.endpoints.objects_api.spaces;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNSpaceFields;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.PNResourceType;
import com.pubnub.api.managers.token_manager.TokenManagerProperties;
import com.pubnub.api.managers.token_manager.TokenManagerPropertyProvider;
import com.pubnub.api.models.consumer.objects_api.space.PNGetSpaceResult;
import com.pubnub.api.models.consumer.objects_api.space.PNSpace;
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
public class GetSpace extends Endpoint<EntityEnvelope<PNSpace>, PNGetSpaceResult>
        implements InclusionParamsProvider<GetSpace, PNSpaceFields>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;

    @Setter
    private String spaceId;

    public GetSpace(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
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

        if (this.spaceId == null || this.spaceId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SPACE_ID_MISSING).build();
        }
    }

    @Override
    protected Call<EntityEnvelope<PNSpace>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getSpaceService()
                .getSpace(this.getPubnub().getConfiguration().getSubscribeKey(), spaceId, params);
    }

    @Override
    protected PNGetSpaceResult createResponse(Response<EntityEnvelope<PNSpace>> input) throws PubNubException {
        PNGetSpaceResult.PNGetSpaceResultBuilder resultBuilder = PNGetSpaceResult.builder();

        if (input.body() != null) {
            resultBuilder.space(input.body().getData());
        }
        return resultBuilder.build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetSpaceOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public GetSpace includeFields(PNSpaceFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public TokenManagerProperties getTmsProperties() {
        return TokenManagerProperties.builder()
                .pnResourceType(PNResourceType.SPACE)
                .resourceId(spaceId)
                .build();
    }
}
