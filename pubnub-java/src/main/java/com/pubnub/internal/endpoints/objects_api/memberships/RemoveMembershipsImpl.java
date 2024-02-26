package com.pubnub.internal.endpoints.objects_api.memberships;

import com.pubnub.api.endpoints.objects_api.memberships.RemoveMemberships;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.models.consumer.objects_api.membership.PNRemoveMembershipResult;
import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveMembershipsImpl extends DelegatingEndpoint<PNRemoveMembershipResult> implements RemoveMemberships {
    private final Collection<PNChannelMembership> channelMemberships;
    private String uuid;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNChannelDetailsLevel includeChannel;

    public RemoveMembershipsImpl(@NotNull Collection<PNChannelMembership> channelMemberships, final InternalPubNubClient pubnubInstance) {
        super(pubnubInstance);
        this.channelMemberships = channelMemberships;
    }

    @Override
    protected ExtendedRemoteAction<PNRemoveMembershipResult> createAction() {
        ArrayList<String> channelList = new ArrayList<>(channelMemberships.size());
        for (PNChannelMembership channel : channelMemberships) {
            channelList.add(channel.getChannel().getId());
        }
        return new MappingRemoteAction<>(
                pubnub.removeMemberships(
                        channelList,
                        uuid,
                        limit,
                        page,
                        filter,
                        SetMembershipsImpl.toInternal(sort),
                        includeTotalCount,
                        includeCustom,
                        SetMembershipsImpl.toInternal(includeChannel)
                ),
                PNRemoveMembershipResult::from);
    }

    @AllArgsConstructor
    public static class Builder implements RemoveMemberships.Builder {
        private final InternalPubNubClient pubnubInstance;

        @Override
        public RemoveMemberships channelMemberships(@NotNull final Collection<PNChannelMembership> channelMemberships) {
            return new RemoveMembershipsImpl(channelMemberships, pubnubInstance);
        }
    }
}
