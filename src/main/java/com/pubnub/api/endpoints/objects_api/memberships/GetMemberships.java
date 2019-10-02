package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNMembershipFields;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.PNResourceType;
import com.pubnub.api.managers.token_manager.TokenManagerProperties;
import com.pubnub.api.managers.token_manager.TokenManagerPropertyProvider;
import com.pubnub.api.models.consumer.objects_api.membership.PNGetMembershipsResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.models.consumer.objects_api.util.InclusionParamsProvider;
import com.pubnub.api.models.consumer.objects_api.util.ListingParamsProvider;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Get a user's inclusionParamList of space membership
 */
@Accessors(chain = true, fluent = true)
public class GetMemberships extends Endpoint<EntityArrayEnvelope<PNMembership>, PNGetMembershipsResult> implements
        InclusionParamsProvider<GetMemberships, PNMembershipFields>,
        ListingParamsProvider<GetMemberships>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;

    @Setter
    private String userId;

    public GetMemberships(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
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
    protected Call<EntityArrayEnvelope<PNMembership>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getMembershipService()
                .getMemberships(this.getPubnub().getConfiguration().getSubscribeKey(), userId, params);
    }

    @Override
    protected PNGetMembershipsResult createResponse(Response<EntityArrayEnvelope<PNMembership>> input) {
        if (input.body() != null) {
            return PNGetMembershipsResult.create(input.body());
        }
        return PNGetMembershipsResult.create();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetMemberships;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public GetMemberships includeFields(PNMembershipFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public GetMemberships limit(Integer limit) {
        return appendLimitParam(extraParamsMap, limit);
    }

    @Override
    public GetMemberships start(String start) {
        extraParamsMap.put("start", start);
        return this;
    }

    @Override
    public GetMemberships end(String end) {
        extraParamsMap.put("end", end);
        return this;
    }

    @Override
    public GetMemberships withTotalCount(Boolean count) {
        extraParamsMap.put("count", String.valueOf(count));
        return this;
    }

    @Override
    public TokenManagerProperties getTmsProperties() {
        return TokenManagerProperties.builder()
                .pnResourceType(PNResourceType.USER)
                .resourceId(userId)
                .build();
    }
}
