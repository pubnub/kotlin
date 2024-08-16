package com.pubnub.internal.java.endpoints.objects_api.memberships;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.memberships.RemoveMemberships;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNRemoveMembershipResult;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveMembershipsImpl extends DelegatingEndpoint<PNChannelMembershipArrayResult, PNRemoveMembershipResult> implements RemoveMemberships {
    private final Collection<PNChannelMembership> channelMemberships;
    private String uuid;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private boolean includeType;
    private Include.PNChannelDetailsLevel includeChannel;

    public RemoveMembershipsImpl(@NotNull Collection<PNChannelMembership> channelMemberships, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channelMemberships = channelMemberships;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNRemoveMembershipResult> mapResult(@NotNull ExtendedRemoteAction<PNChannelMembershipArrayResult> action) {
        return new MappingRemoteAction<>(action, PNRemoveMembershipResult::from);
    }

    @Override
    @NotNull
    protected Endpoint<PNChannelMembershipArrayResult> createAction() {
        ArrayList<String> channelList = new ArrayList<>(channelMemberships.size());
        for (PNChannelMembership channel : channelMemberships) {
            channelList.add(channel.getChannel().getId());
        }
        return pubnub.removeMemberships(
                channelList,
                uuid,
                limit,
                page,
                filter,
                SetMembershipsImpl.toInternal(sort),
                includeTotalCount,
                includeCustom,
                SetMembershipsImpl.toInternal(includeChannel),
                includeType
        );
    }

    public static class Builder implements RemoveMemberships.Builder {
        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public RemoveMemberships channelMemberships(@NotNull final Collection<PNChannelMembership> channelMemberships) {
            return new RemoveMembershipsImpl(channelMemberships, pubnubInstance);
        }
    }
}
