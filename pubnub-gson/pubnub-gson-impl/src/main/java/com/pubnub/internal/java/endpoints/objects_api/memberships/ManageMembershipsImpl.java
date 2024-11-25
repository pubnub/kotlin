package com.pubnub.internal.java.endpoints.objects_api.memberships;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.memberships.ManageMemberships;
import com.pubnub.api.java.endpoints.objects_api.memberships.ManageMembershipsBuilder;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNManageMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNManageMembershipResultConverter;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput;
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static com.pubnub.internal.java.endpoints.objects_api.memberships.SetMembershipsImpl.getMembershipInclude;

@Setter
@Accessors(chain = true, fluent = true)
public class ManageMembershipsImpl extends DelegatingEndpoint<PNChannelMembershipArrayResult, PNManageMembershipResult> implements ManageMemberships, ManageMembershipsBuilder {
    private Collection<PNChannelMembership> set;
    private Collection<PNChannelMembership> remove;
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

    public ManageMembershipsImpl(Collection<PNChannelMembership> channelsToSet, Collection<PNChannelMembership> channelsToRemove, final PubNub pubnubInstance) {
        super(pubnubInstance);
        set = channelsToSet;
        remove = channelsToRemove;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNManageMembershipResult> mapResult(@NotNull ExtendedRemoteAction<PNChannelMembershipArrayResult> action) {
        return new MappingRemoteAction<>(action, PNManageMembershipResultConverter::from);
    }

    @Override
    @NotNull
    protected Endpoint<PNChannelMembershipArrayResult> createRemoteAction() {
        ArrayList<ChannelMembershipInput> toSet = new ArrayList<>(set.size());
        for (PNChannelMembership channel : set) {
            toSet.add(new com.pubnub.api.models.consumer.objects.membership.PNChannelMembership.Partial(
                    channel.getChannel().getId(),
                    channel.getCustom(), // despite IDE error this works ¯\_(ツ)_/¯
                    channel.getStatus(),
                    channel.getType()
            ));
        }
        ArrayList<String> toRemove = new ArrayList<>(remove.size());
        for (PNChannelMembership channel : remove) {
            toRemove.add(channel.getChannel().getId());
        }

        return
                pubnub.manageMemberships(
                        toSet,
                        toRemove,
                        getUserId(),
                        limit,
                        page,
                        filter,
                        SetMembershipsImpl.toInternal(sort),
                        getMembershipInclude(include, includeChannel, includeTotalCount, includeCustom, includeType)
                );
    }

    public static class Builder implements ManageMemberships.Builder {
        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public RemoveStep<ManageMemberships, PNChannelMembership> set(final Collection<PNChannelMembership> channelsToSet) {
            return new RemoveStep<ManageMemberships, PNChannelMembership>() {
                @Override
                public ManageMemberships remove(final Collection<PNChannelMembership> channelsToRemove) {
                    return new ManageMembershipsImpl(channelsToSet,
                            channelsToRemove,
                            pubnubInstance);
                }
            };
        }

        @Override
        public SetStep<ManageMemberships, PNChannelMembership> remove(final Collection<PNChannelMembership> channelsToRemove) {
            return new SetStep<ManageMemberships, PNChannelMembership>() {
                @Override
                public ManageMemberships set(final Collection<PNChannelMembership> channelsToSet) {
                    return new ManageMembershipsImpl(channelsToSet,
                            channelsToRemove,
                            pubnubInstance);
                }
            };
        }
    }

    private String getUserId() {
        return userId != null ? userId : uuid;
    }
}
