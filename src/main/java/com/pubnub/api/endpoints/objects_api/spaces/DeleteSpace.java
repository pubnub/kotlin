package com.pubnub.api.endpoints.objects_api.spaces;

import com.google.gson.JsonElement;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.PNResourceType;
import com.pubnub.api.managers.token_manager.TokenManagerProperties;
import com.pubnub.api.managers.token_manager.TokenManagerPropertyProvider;
import com.pubnub.api.models.consumer.objects_api.space.PNDeleteSpaceResult;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class DeleteSpace extends Endpoint<EntityEnvelope<JsonElement>, PNDeleteSpaceResult>
        implements TokenManagerPropertyProvider {

    @Setter
    private String spaceId;

    public DeleteSpace(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
        super(pubnubInstance, telemetry, retrofitInstance);
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
    protected Call<EntityEnvelope<JsonElement>> doWork(Map<String, String> params) {

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getSpaceService()
                .deleteSpace(this.getPubnub().getConfiguration().getSubscribeKey(), spaceId, params);
    }

    @Override
    protected PNDeleteSpaceResult createResponse(Response<EntityEnvelope<JsonElement>> input) throws PubNubException {
        return PNDeleteSpaceResult.builder().build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNDeleteSpaceOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public TokenManagerProperties getTmsProperties() {
        return TokenManagerProperties.builder()
                .pnResourceType(PNResourceType.SPACE)
                .resourceId(spaceId)
                .build();
    }
}
