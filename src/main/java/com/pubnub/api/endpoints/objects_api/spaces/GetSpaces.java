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
import com.pubnub.api.models.consumer.objects_api.space.PNGetSpacesResult;
import com.pubnub.api.models.consumer.objects_api.space.PNSpace;
import com.pubnub.api.models.consumer.objects_api.util.InclusionParamsProvider;
import com.pubnub.api.models.consumer.objects_api.util.ListingParamsProvider;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class GetSpaces extends Endpoint<EntityArrayEnvelope<PNSpace>, PNGetSpacesResult> implements
        InclusionParamsProvider<GetSpaces, PNSpaceFields>,
        ListingParamsProvider<GetSpaces>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;

    public GetSpaces(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
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
    }

    @Override
    protected Call<EntityArrayEnvelope<PNSpace>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getSpaceService()
                .getSpaces(this.getPubnub().getConfiguration().getSubscribeKey(), params);
    }

    @Override
    protected PNGetSpacesResult createResponse(Response<EntityArrayEnvelope<PNSpace>> input) throws PubNubException {
        if (input.body() != null) {
            return PNGetSpacesResult.create(input.body());
        }
        return PNGetSpacesResult.create();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetSpacesOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public GetSpaces includeFields(PNSpaceFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public GetSpaces limit(Integer limit) {
        return appendLimitParam(extraParamsMap, limit);
    }

    @Override
    public GetSpaces start(String start) {
        extraParamsMap.put("start", start);
        return this;
    }

    @Override
    public GetSpaces end(String end) {
        extraParamsMap.put("end", end);
        return this;
    }

    @Override
    public GetSpaces withTotalCount(Boolean count) {
        extraParamsMap.put("count", String.valueOf(count));
        return this;
    }


    @Override
    public TokenManagerProperties getTmsProperties() {
        return TokenManagerProperties.builder()
                .pnResourceType(PNResourceType.SPACE)
                .resourceId("")
                .build();
    }
}
