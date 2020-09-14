package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.objects_api.CompositeParameterEnricher;
import com.pubnub.api.endpoints.objects_api.UUIDEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include.ChannelIncludeAware;
import com.pubnub.api.endpoints.objects_api.utils.Include.CustomIncludeAware;
import com.pubnub.api.endpoints.objects_api.utils.Include.HavingChannelInclude;
import com.pubnub.api.endpoints.objects_api.utils.Include.HavingCustomInclude;
import com.pubnub.api.endpoints.objects_api.utils.ListCapabilities.HavingListCapabilites;
import com.pubnub.api.endpoints.objects_api.utils.ListCapabilities.ListCapabilitiesAware;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.models.consumer.objects_api.membership.PNGetMembershipsResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Map;

public abstract class GetMemberships extends UUIDEndpoint<GetMemberships, EntityArrayEnvelope<PNMembership>, PNGetMembershipsResult>
        implements CustomIncludeAware<GetMemberships>, ChannelIncludeAware<GetMemberships>,
        ListCapabilitiesAware<GetMemberships> {

    public GetMemberships(final PubNub pubnubInstance,
                          final TelemetryManager telemetry,
                          final RetrofitManager retrofitInstance,
                          final CompositeParameterEnricher compositeParameterEnricher) {
        super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }

    public static GetMemberships create(final PubNub pubnubInstance,
                                        final TelemetryManager telemetry,
                                        final RetrofitManager retrofitInstance) {
        final CompositeParameterEnricher compositeParameterEnricher = CompositeParameterEnricher.createDefault();
        return new GetMembershipsCommand(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }
}

final class GetMembershipsCommand extends GetMemberships
        implements HavingCustomInclude<GetMemberships>, HavingChannelInclude<GetMemberships>,
        HavingListCapabilites<GetMemberships> {
    GetMembershipsCommand(final PubNub pubnubInstance,
                          final TelemetryManager telemetry,
                          final RetrofitManager retrofitInstance,
                          final CompositeParameterEnricher compositeParameterEnricher) {
        super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher);
    }

    @Override
    protected Call<EntityArrayEnvelope<PNMembership>> executeCommand(final Map<String, String> effectiveParams)
            throws PubNubException {
        return getRetrofit()
                .getUuidMetadataService()
                .getMemberships(getPubnub().getConfiguration().getSubscribeKey(), effectiveUuid(), effectiveParams);

    }

    @Override
    protected PNGetMembershipsResult createResponse(Response<EntityArrayEnvelope<PNMembership>> input)
            throws PubNubException {
        if (input.body() != null) {
            return new PNGetMembershipsResult(input.body());
        } else {
            return new PNGetMembershipsResult();
        }
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetMembershipsOperation;
    }

    @Override
    public CompositeParameterEnricher getCompositeParameterEnricher() {
        return super.getCompositeParameterEnricher();
    }
}


