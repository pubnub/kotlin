package com.pubnub.internal.java.endpoints.objects_api.memberships;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.memberships.GetMemberships;
import com.pubnub.api.java.endpoints.objects_api.memberships.GetMembershipsBuilder;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNGetMembershipsResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNGetMembershipsResultConverter;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMembershipsImpl extends DelegatingEndpoint<PNChannelMembershipArrayResult, PNGetMembershipsResult> implements GetMemberships, GetMembershipsBuilder {
    private String uuid; // deprecated
    private String userId;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private boolean includeType;
    private Include.PNChannelDetailsLevel includeChannel;
    private MembershipInclude include;

    @Deprecated
    public GetMembershipsImpl(PubNub pubnub) {
        super(pubnub);
    }

    public GetMembershipsImpl(PubNub pubnub, String userId){
        super(pubnub);
        this.uuid = userId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetMembershipsResult> mapResult(@NotNull ExtendedRemoteAction<PNChannelMembershipArrayResult> action) {
        return new MappingRemoteAction<>(action, PNGetMembershipsResultConverter::from);
    }

    @Override
    @NotNull
    protected Endpoint<PNChannelMembershipArrayResult> createRemoteAction() {
        return pubnub.getMemberships(
                getUserId(),
                limit,
                page,
                filter,
                SetMembershipsImpl.toInternal(sort),
                getMembershipInclude()
        );
    }

    private String getUserId() {
        return userId != null ? userId : uuid;
    }

    private MembershipInclude getMembershipInclude() {
        if (include != null) {
            return include;
        } else {
            // if deprecated setMembership API used
            if (includeChannel == Include.PNChannelDetailsLevel.CHANNEL) {
                return MembershipInclude.builder()
                        .includeTotalCount(includeTotalCount)
                        .includeCustom(includeCustom)
                        .includeType(includeType)
                        .includeChannel(true)
                        .build();
            } else if (includeChannel == Include.PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM) {
                return MembershipInclude.builder()
                        .includeTotalCount(includeTotalCount)
                        .includeCustom(includeCustom)
                        .includeType(includeType)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .build();
            } else {
                return MembershipInclude.builder()
                        .includeTotalCount(includeTotalCount)
                        .includeCustom(includeCustom)
                        .includeType(includeType)
                        .build();
            }
        }
    }
}


