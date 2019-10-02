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
import com.pubnub.api.models.consumer.objects_api.PNPatchPayload;
import com.pubnub.api.models.consumer.objects_api.membership.Membership;
import com.pubnub.api.models.consumer.objects_api.membership.PNManageMembershipsResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.models.consumer.objects_api.util.InclusionParamsProvider;
import com.pubnub.api.models.consumer.objects_api.util.ListingParamsProvider;
import com.pubnub.api.models.consumer.objects_api.util.MembershipChainProvider;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class ManageMemberships extends Endpoint<EntityArrayEnvelope<PNMembership>, PNManageMembershipsResult> implements
        InclusionParamsProvider<ManageMemberships, PNMembershipFields>,
        ListingParamsProvider<ManageMemberships>,
        MembershipChainProvider<ManageMemberships, Membership>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;
    private PNPatchPayload<Membership> pnPatchPayload;

    @Setter
    private String userId;

    public ManageMemberships(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
        super(pubnubInstance, telemetry, retrofitInstance);
        extraParamsMap = new HashMap<>();
        pnPatchPayload = new PNPatchPayload<>();
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
                .manageMemberships(this.getPubnub().getConfiguration().getSubscribeKey(), userId, pnPatchPayload,
                        params);
    }

    @Override
    protected PNManageMembershipsResult createResponse(Response<EntityArrayEnvelope<PNMembership>> input) throws PubNubException {
        if (input.body() != null) {
            return PNManageMembershipsResult.create(input.body());
        }
        return PNManageMembershipsResult.create();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNManageMemberships;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public ManageMemberships includeFields(PNMembershipFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public ManageMemberships add(Membership... list) {
        pnPatchPayload.setAdd(list);
        return this;
    }

    @Override
    public ManageMemberships update(Membership... list) {
        pnPatchPayload.setUpdate(list);
        return this;
    }

    @Override
    public ManageMemberships remove(Membership... list) {
        pnPatchPayload.setRemove(list);
        return this;
    }

    @Override
    public ManageMemberships limit(Integer limit) {
        return appendLimitParam(extraParamsMap, limit);
    }

    @Override
    public ManageMemberships start(String start) {
        extraParamsMap.put("start", start);
        return this;
    }

    @Override
    public ManageMemberships end(String end) {
        extraParamsMap.put("end", end);
        return this;
    }

    @Override
    public ManageMemberships withTotalCount(Boolean count) {
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
