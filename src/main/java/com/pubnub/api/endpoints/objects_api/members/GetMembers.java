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
import com.pubnub.api.models.consumer.objects_api.member.PNGetMembersResult;
import com.pubnub.api.models.consumer.objects_api.member.PNMember;
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
 * Get the inclusionParamList of members in a space
 */
@Accessors(chain = true, fluent = true)
public class GetMembers extends Endpoint<EntityArrayEnvelope<PNMember>, PNGetMembersResult> implements
        InclusionParamsProvider<GetMembers, PNMemberFields>,
        ListingParamsProvider<GetMembers>,
        TokenManagerPropertyProvider {

    private Map<String, String> extraParamsMap;

    @Setter
    private String spaceId;

    public GetMembers(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
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
    protected Call<EntityArrayEnvelope<PNMember>> doWork(Map<String, String> params) {

        params.putAll(extraParamsMap);

        params.putAll(encodeParams(params));

        return this.getRetrofit()
                .getMemberService()
                .getMembers(this.getPubnub().getConfiguration().getSubscribeKey(), spaceId, params);
    }

    @Override
    protected PNGetMembersResult createResponse(Response<EntityArrayEnvelope<PNMember>> input) {
        if (input.body() != null) {
            return PNGetMembersResult.create(input.body());
        }
        return PNGetMembersResult.create();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNGetMembers;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    public GetMembers includeFields(PNMemberFields... params) {
        return appendInclusionParams(extraParamsMap, params);
    }

    @Override
    public GetMembers limit(Integer limit) {
        return appendLimitParam(extraParamsMap, limit);
    }

    @Override
    public GetMembers start(String start) {
        extraParamsMap.put("start", start);
        return this;
    }

    @Override
    public GetMembers end(String end) {
        extraParamsMap.put("end", end);
        return this;
    }

    @Override
    public GetMembers withTotalCount(Boolean count) {
        extraParamsMap.put("count", String.valueOf(count));
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
