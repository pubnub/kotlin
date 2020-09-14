package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.objects_api.ChannelEnpoint;
import com.pubnub.api.endpoints.objects_api.CompositeParameterEnricher;
import com.pubnub.api.endpoints.objects_api.utils.Include.CustomIncludeAware;
import com.pubnub.api.endpoints.objects_api.utils.Include.HavingCustomInclude;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetChannelMetadataResult;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Map;

public abstract class GetChannelMetadata
        extends ChannelEnpoint<EntityEnvelope<PNChannelMetadata>, PNGetChannelMetadataResult>
        implements CustomIncludeAware<GetChannelMetadata> {
    GetChannelMetadata(final String channel,
                              final PubNub pubnubInstance,
                              final TelemetryManager telemetry,
                              final RetrofitManager retrofitInstance,
                              final CompositeParameterEnricher compositeParameterEnricher) {
        super(channel, pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }

    public static Builder builder(final PubNub pubnubInstance,
                                  final TelemetryManager telemetry,
                                  final RetrofitManager retrofitInstance) {
        return new Builder(pubnubInstance, telemetry, retrofitInstance);
    }

    @AllArgsConstructor
    public static class Builder implements BuilderSteps.ChannelStep<GetChannelMetadata> {
        private final PubNub pubnubInstance;
        private final TelemetryManager telemetry;
        private final RetrofitManager retrofitInstance;

        @Override
        public GetChannelMetadata channel(final String channel) {
            final CompositeParameterEnricher compositeParameterEnricher = CompositeParameterEnricher.createDefault();
            return new GetChannelMetadataCommand(channel, pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
        }
    }
}

final class GetChannelMetadataCommand extends GetChannelMetadata implements HavingCustomInclude<GetChannelMetadata> {
    GetChannelMetadataCommand(final String channel,
                                     final PubNub pubnubInstance,
                                     final TelemetryManager telemetry,
                                     final RetrofitManager retrofitInstance,
                                     final CompositeParameterEnricher compositeParameterEnricher) {
        super(channel, pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }

    @Override
    protected Call<EntityEnvelope<PNChannelMetadata>> executeCommand(final Map<String, String> effectiveParams)
            throws PubNubException {
        return getRetrofit()
                .getChannelMetadataService()
                .getChannelMetadata(getPubnub().getConfiguration().getSubscribeKey(), channel, effectiveParams);
    }

    @Override
    protected PNGetChannelMetadataResult createResponse(Response<EntityEnvelope<PNChannelMetadata>> input)
            throws PubNubException {
        if (input.body() != null) {
            return new PNGetChannelMetadataResult(input.body());
        } else {
            return new PNGetChannelMetadataResult();
        }
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetChannelMetadataOperation;
    }

    @Override
    public CompositeParameterEnricher getCompositeParameterEnricher() {
        return super.getCompositeParameterEnricher();
    }
}
