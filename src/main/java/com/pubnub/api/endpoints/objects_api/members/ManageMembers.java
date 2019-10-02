package com.pubnub.api.endpoints.objects_api.members;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNMemberFields;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.PNResourceType;
import com.pubnub.api.managers.token_manager.TokenManagerProperties;
import com.pubnub.api.managers.token_manager.TokenManagerPropertyProvider;
import com.pubnub.api.models.consumer.objects_api.PNPatchPayload;
import com.pubnub.api.models.consumer.objects_api.member.Member;
import com.pubnub.api.models.consumer.objects_api.member.PNManageMembersResult;
import com.pubnub.api.models.consumer.objects_api.member.PNMember;
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
public class ManageMembers extends Endpoint<EntityArrayEnvelope<PNMember>, PNManageMembersResult> implements
        InclusionParamsProvider<ManageMembers, PNMemberFields>,
        ListingParamsProvider<ManageMembers>,
        MembershipChainProvider<ManageMembers, Member>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;
    private PNPatchPayload<Member> pnPatchPayload;

    @Setter
    private String spaceId;

    public ManageMembers(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
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

        if (this.spaceId == null || this.spaceId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SPACE_ID_MISSING).build();
        }
    }

    @Override
    protected Call<EntityArrayEnvelope<PNMember>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getMemberService()
                .manageMembers(this.getPubnub().getConfiguration().getSubscribeKey(), spaceId, pnPatchPayload, params);
    }

    @Override
    protected PNManageMembersResult createResponse(Response<EntityArrayEnvelope<PNMember>> input)
            throws PubNubException {
        if (input.body() != null) {
            return PNManageMembersResult.create(input.body());
        }
        return PNManageMembersResult.create();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNManageMembers;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public ManageMembers includeFields(PNMemberFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public ManageMembers limit(Integer limit) {
        return appendLimitParam(extraParamsMap, limit);
    }

    @Override
    public ManageMembers start(String start) {
        extraParamsMap.put("start", start);
        return this;
    }

    @Override
    public ManageMembers end(String end) {
        extraParamsMap.put("end", end);
        return this;
    }

    @Override
    public ManageMembers withTotalCount(Boolean count) {
        extraParamsMap.put("count", String.valueOf(count));
        return this;
    }

    @Override
    public ManageMembers add(Member... list) {
        pnPatchPayload.setAdd(list);
        return this;
    }

    @Override
    public ManageMembers update(Member... list) {
        pnPatchPayload.setUpdate(list);
        return this;
    }

    @Override
    public ManageMembers remove(Member... list) {
        pnPatchPayload.setRemove(list);
        return this;
    }

    @Override
    public TokenManagerProperties getTmsProperties() {
        return TokenManagerProperties.builder()
                .pnResourceType(PNResourceType.SPACE)
                .resourceId(spaceId)
                .build();
    }
}


