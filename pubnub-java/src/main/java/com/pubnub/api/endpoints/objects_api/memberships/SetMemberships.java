package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.models.consumer.objects_api.membership.PNSetMembershipResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import com.pubnub.internal.models.consumer.objects.PNMembershipKey;
import com.pubnub.internal.models.consumer.objects.membership.ChannelMembershipInput;
import com.pubnub.internal.models.consumer.objects.membership.PNChannelMembership.Partial;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class SetMemberships extends Endpoint<PNSetMembershipResult> {
    private final Collection<PNChannelMembership> channels;
    private String uuid;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNChannelDetailsLevel includeChannel;

    public SetMemberships(@NotNull Collection<PNChannelMembership> channelMemberships, final PubNubImpl pubnubInstance) {
        super(pubnubInstance);
        this.channels = channelMemberships;
    }

    @Override
    protected ExtendedRemoteAction<PNSetMembershipResult> createAction() {
        ArrayList<ChannelMembershipInput> channelList = new ArrayList<>(channels.size());
        for (PNChannelMembership channel : channels) {
            channelList.add(new Partial(
                    channel.getChannel().getId(),
                    (channel instanceof PNChannelMembership.ChannelWithCustom)
                            ? ((PNChannelMembership.ChannelWithCustom) channel).getCustom()
                            : null,
                    null
            ));
        }
        return new MappingRemoteAction<>(
                pubnub.setMemberships(
                        channelList,
                        uuid,
                        limit,
                        page,
                        filter,
                        toInternal(sort),
                        includeTotalCount,
                        includeCustom,
                        toInternal(includeChannel)
                ),
                PNSetMembershipResult::from
        );
    }

    public static Builder builder(final PubNubImpl pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder {
        private final PubNubImpl pubnubInstance;

        public SetMemberships channelMemberships(@NotNull final Collection<PNChannelMembership> channelMemberships) {
            return new SetMemberships(channelMemberships, pubnubInstance);
        }
    }

    @Nullable
    static com.pubnub.internal.models.consumer.objects.membership.PNChannelDetailsLevel toInternal(@Nullable Include.PNChannelDetailsLevel detailLevel) {
        if (detailLevel == null) {
            return null;
        }
        switch (detailLevel) {
            case CHANNEL:
                return com.pubnub.internal.models.consumer.objects.membership.PNChannelDetailsLevel.CHANNEL;
            case CHANNEL_WITH_CUSTOM:
                return com.pubnub.internal.models.consumer.objects.membership.PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM;
            default:
                throw new IllegalStateException("Unknown detail level: " + detailLevel);
        }
    }

    static Collection<? extends com.pubnub.internal.models.consumer.objects.PNSortKey<PNMembershipKey>> toInternal(Collection<PNSortKey> sort) {
        List<com.pubnub.internal.models.consumer.objects.PNSortKey<PNMembershipKey>> list = new ArrayList<>(sort.size());
        for (PNSortKey pnSortKey : sort) {
            PNMembershipKey key = null;
            switch (pnSortKey.getKey()) {
                case ID:
                    key = PNMembershipKey.CHANNEL_ID;
                    break;
                case NAME:
                    key = PNMembershipKey.CHANNEL_NAME;
                    break;
                case UPDATED:
                    key = PNMembershipKey.CHANNEL_UPDATED;
                    break;
            }
            if (pnSortKey.getDir().equals(PNSortKey.Dir.ASC)) {
                list.add(new com.pubnub.internal.models.consumer.objects.PNSortKey.PNAsc<>(key));
            } else {
                list.add(new com.pubnub.internal.models.consumer.objects.PNSortKey.PNDesc<>(key));
            }
        }
        return list;
    }
}
