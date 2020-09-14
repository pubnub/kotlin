package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.objects_api.CompositeParameterEnricher;
import com.pubnub.api.endpoints.objects_api.ObjectApiEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include.CustomIncludeAware;
import com.pubnub.api.endpoints.objects_api.utils.Include.HavingCustomInclude;
import com.pubnub.api.endpoints.objects_api.utils.ListCapabilities.HavingListCapabilites;
import com.pubnub.api.endpoints.objects_api.utils.ListCapabilities.ListCapabilitiesAware;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.consumer.objects_api.uuid.PNGetAllUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Map;

public abstract class GetAllUUIDMetadata
        extends ObjectApiEndpoint<EntityArrayEnvelope<PNUUIDMetadata>, PNGetAllUUIDMetadataResult> implements
        CustomIncludeAware<GetAllUUIDMetadata>,
        ListCapabilitiesAware<GetAllUUIDMetadata> {

    public GetAllUUIDMetadata(final PubNub pubnubInstance,
                              final TelemetryManager telemetry,
                              final RetrofitManager retrofitInstance,
                              final CompositeParameterEnricher compositeParameterEnricher) {
        super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }

    public static GetAllUUIDMetadata create(final PubNub pubnubInstance,
                                            final TelemetryManager telemetry,
                                            final RetrofitManager retrofitInstance) {
        final CompositeParameterEnricher compositeParameterEnricher = CompositeParameterEnricher.createDefault();
        return new GetAllUUIDMetadataCommand(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }
}

final class GetAllUUIDMetadataCommand extends GetAllUUIDMetadata implements
        HavingCustomInclude<GetAllUUIDMetadata>, HavingListCapabilites<GetAllUUIDMetadata> {

    GetAllUUIDMetadataCommand(final PubNub pubnubInstance,
                              final TelemetryManager telemetry,
                              final RetrofitManager retrofitInstance,
                              final CompositeParameterEnricher compositeParameterEnricher) {
        super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }

    @Override
    protected Call<EntityArrayEnvelope<PNUUIDMetadata>> executeCommand(final Map<String, String> effectiveParams)
            throws PubNubException {
        return getRetrofit()
                .getUuidMetadataService()
                .getUUIDMetadata(getPubnub().getConfiguration().getSubscribeKey(), effectiveParams);
    }

    @Override
    protected PNGetAllUUIDMetadataResult createResponse(Response<EntityArrayEnvelope<PNUUIDMetadata>> input)
            throws PubNubException {
        if (input.body() != null) {
            return new PNGetAllUUIDMetadataResult(input.body());
        } else {
            return new PNGetAllUUIDMetadataResult();
        }
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetAllUuidMetadataOperation;
    }

    @Override
    public CompositeParameterEnricher getCompositeParameterEnricher() {
        return super.getCompositeParameterEnricher();
    }
}
