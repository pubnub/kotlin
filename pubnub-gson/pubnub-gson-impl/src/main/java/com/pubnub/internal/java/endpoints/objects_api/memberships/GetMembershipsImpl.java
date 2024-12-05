package com.pubnub.internal.java.endpoints.objects_api.memberships;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.memberships.GetMemberships;
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

import static com.pubnub.internal.java.endpoints.objects_api.memberships.SetMembershipsImpl.getMembershipInclude;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMembershipsImpl extends DelegatingEndpoint<PNChannelMembershipArrayResult, PNGetMembershipsResult> implements GetMemberships {
    private String uuid; // deprecated
    private String userId;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount; // deprecated
    private boolean includeCustom; // deprecated
    private boolean includeType; // deprecated
    private Include.PNChannelDetailsLevel includeChannel; // deprecated
    private MembershipInclude include;

    public GetMembershipsImpl(PubNub pubnub) {
        super(pubnub);
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
                getMembershipInclude(include, includeChannel, includeTotalCount, includeCustom, includeType)
        );
    }

    private String getUserId() {
        return userId != null ? userId : uuid;
    }
}


