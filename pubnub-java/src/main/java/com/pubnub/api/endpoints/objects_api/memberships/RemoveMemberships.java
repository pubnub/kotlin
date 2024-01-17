package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.models.consumer.objects_api.membership.PNRemoveMembershipResult;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveMemberships extends ValidatingEndpoint<PNRemoveMembershipResult> {
    private final Collection<PNChannelMembership> channelMemberships;
    private String uuid;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNChannelDetailsLevel includeChannel;

    public RemoveMemberships(@NotNull Collection<PNChannelMembership> channelMemberships, final com.pubnub.internal.PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channelMemberships = channelMemberships;
    }

    @Override
    protected Endpoint<PNRemoveMembershipResult> createAction() {
        ArrayList<String> channelList = new ArrayList<>(channelMemberships.size());
        for (PNChannelMembership channel : channelMemberships) {
            channelList.add(channel.getChannel().getId());
        }
        return new MappingEndpoint<>(
                pubnub.removeMemberships(
                        channelList,
                        uuid,
                        limit,
                        page,
                        filter,
                        SetMemberships.toInternal(sort),
                        includeTotalCount,
                        includeCustom,
                        SetMemberships.toInternal(includeChannel)
                ),
                PNRemoveMembershipResult::from);
    }

    public static Builder builder(final com.pubnub.internal.PubNub pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements ObjectsBuilderSteps.ChannelMembershipsStep<RemoveMemberships> {
        private final com.pubnub.internal.PubNub pubnubInstance;

        @Override
        public RemoveMemberships channelMemberships(@NotNull final Collection<PNChannelMembership> channelMemberships) {
            return new RemoveMemberships(channelMemberships, pubnubInstance);
        }
    }
}
