package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.models.consumer.objects_api.membership.PNManageMembershipResult;
import com.pubnub.internal.PubNubImpl;
import com.pubnub.internal.models.consumer.objects.membership.ChannelMembershipInput;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class ManageMemberships extends Endpoint<PNManageMembershipResult> {
    private Collection<PNChannelMembership> set;
    private Collection<PNChannelMembership> remove;
    private String uuid;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNChannelDetailsLevel includeChannel;

    public ManageMemberships(Collection<PNChannelMembership> channelsToSet, Collection<PNChannelMembership> channelsToRemove, final PubNubImpl pubnubInstance) {
        super(pubnubInstance);
    }

    @Override
    protected ExtendedRemoteAction<PNManageMembershipResult> createAction() {
        ArrayList<ChannelMembershipInput> toSet = new ArrayList<>(set.size());
        for (PNChannelMembership channel : set) {
            toSet.add(new com.pubnub.internal.models.consumer.objects.membership.PNChannelMembership.Partial(
                    channel.getChannel().getId(),
                    (channel instanceof PNChannelMembership.ChannelWithCustom)
                            ? ((PNChannelMembership.ChannelWithCustom) channel).getCustom()
                            : null,
                    null
            ));
        }
        ArrayList<String> toRemove = new ArrayList<>(remove.size());
        for (PNChannelMembership channel : remove) {
            toRemove.add(channel.getChannel().getId());
        }

        return new MappingRemoteAction<>(
                pubnub.manageMemberships(
                        toSet,
                        toRemove,
                        uuid,
                        limit,
                        page,
                        filter,
                        SetMemberships.toInternal(sort),
                        includeTotalCount,
                        includeCustom,
                        SetMemberships.toInternal(includeChannel)
                ),
                PNManageMembershipResult::from);
    }

    public static Builder builder(final PubNubImpl pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements ObjectsBuilderSteps.RemoveOrSetStep<ManageMemberships, PNChannelMembership> {
        private final PubNubImpl pubnubInstance;

        @Override
        public RemoveStep<ManageMemberships, PNChannelMembership> set(final Collection<PNChannelMembership> channelsToSet) {
            return new RemoveStep<ManageMemberships, PNChannelMembership>() {
                @Override
                public ManageMemberships remove(final Collection<PNChannelMembership> channelsToRemove) {
                    return new ManageMemberships(channelsToSet,
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
                    return new ManageMemberships(channelsToSet,
                            channelsToRemove,
                            pubnubInstance);
                }
            };
        }
    }
}
