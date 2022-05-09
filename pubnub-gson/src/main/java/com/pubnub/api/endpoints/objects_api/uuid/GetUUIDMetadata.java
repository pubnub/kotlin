package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.objects_api.CompositeParameterEnricher;
import com.pubnub.api.endpoints.objects_api.UUIDEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.Include.CustomIncludeAware;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Map;

public abstract class GetUUIDMetadata extends UUIDEndpoint<GetUUIDMetadata, EntityEnvelope<PNUUIDMetadata>, PNGetUUIDMetadataResult>
        implements CustomIncludeAware<GetUUIDMetadata> {

    public GetUUIDMetadata(final PubNub pubnubInstance,
                           final TelemetryManager telemetry,
                           final RetrofitManager retrofitInstance,
                           final CompositeParameterEnricher compositeParameterEnricher,
                           final TokenManager tokenManager) {
        super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher, tokenManager);
    }

    public static GetUUIDMetadata create(final PubNub pubnubInstance,
                                         final TelemetryManager telemetry,
                                         final RetrofitManager retrofitInstance,
                                         final TokenManager tokenManager) {
        final CompositeParameterEnricher compositeParameterEnricher = CompositeParameterEnricher.createDefault();
        return new GetUUIDMetadataCommand(pubnubInstance, telemetry, retrofitInstance,
                compositeParameterEnricher, tokenManager);
    }
}

final class GetUUIDMetadataCommand extends GetUUIDMetadata implements Include.HavingCustomInclude<GetUUIDMetadata> {
    GetUUIDMetadataCommand(final PubNub pubnubInstance,
                           final TelemetryManager telemetry,
                           final RetrofitManager retrofitInstance,
                           final CompositeParameterEnricher compositeParameterEnricher,
                           final TokenManager tokenManager) {
        super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher, tokenManager);
    }

    @Override
    protected Call<EntityEnvelope<PNUUIDMetadata>> executeCommand(final Map<String, String> effectiveParams)
            throws PubNubException {
        return getRetrofit()
                .getUuidMetadataService()
                .getUUIDMetadata(getPubnub().getConfiguration().getSubscribeKey(), effectiveUuid(), effectiveParams);
    }

    @Override
    protected PNGetUUIDMetadataResult createResponse(Response<EntityEnvelope<PNUUIDMetadata>> input)
            throws PubNubException {
        if (input.body() != null) {
            return new PNGetUUIDMetadataResult(input.body());
        } else {
            return new PNGetUUIDMetadataResult();
        }
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetUuidMetadataOperation;
    }

    @Override
    public CompositeParameterEnricher getCompositeParameterEnricher() {
        return super.getCompositeParameterEnricher();
    }
}
