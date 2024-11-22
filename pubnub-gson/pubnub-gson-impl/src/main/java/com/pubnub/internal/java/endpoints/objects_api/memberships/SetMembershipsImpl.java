package com.pubnub.internal.java.endpoints.objects_api.memberships;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.memberships.SetMemberships;
import com.pubnub.api.java.endpoints.objects_api.memberships.SetMembershipsBuilder;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNSetMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNSetMembershipResultConverter;
import com.pubnub.api.models.consumer.objects.PNMembershipKey;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput;
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership.Partial;
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
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
public class SetMembershipsImpl extends DelegatingEndpoint<PNChannelMembershipArrayResult, PNSetMembershipResult> implements SetMemberships, SetMembershipsBuilder {
    private final Collection<PNChannelMembership> channels;
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

    public SetMembershipsImpl(@NotNull Collection<PNChannelMembership> channelMemberships, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channels = channelMemberships;
    }

    @Override
    @NotNull
    protected Endpoint<PNChannelMembershipArrayResult> createRemoteAction() {
        ArrayList<ChannelMembershipInput> channelList = new ArrayList<>(channels.size());
        for (PNChannelMembership channel : channels) {
            channelList.add(new Partial(
                    channel.getChannel().getId(),
                    channel.getCustom(), //despite IDE error this works
                    channel.getStatus(),
                    channel.getType()
            ));
        }
        return pubnub.setMemberships(
                channelList,
                getUserId(),
                limit,
                page,
                filter,
                toInternal(sort),
                getMembershipInclude()
        );
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNSetMembershipResult> mapResult(@NotNull ExtendedRemoteAction<PNChannelMembershipArrayResult> action) {
        return new MappingRemoteAction<>(action, PNSetMembershipResultConverter::from);
    }

    public static class BuilderDeprecated implements SetMemberships.Builder {
        private final PubNub pubnubInstance;

        public BuilderDeprecated(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        public SetMemberships channelMemberships(@NotNull final Collection<PNChannelMembership> channelMemberships) {
            return new SetMembershipsImpl(channelMemberships, pubnubInstance);
        }
    }

    @Nullable
    static com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel toInternal(@Nullable Include.PNChannelDetailsLevel detailLevel) {
        if (detailLevel == null) {
            return null;
        }
        switch (detailLevel) {
            case CHANNEL:
                return com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel.CHANNEL;
            case CHANNEL_WITH_CUSTOM:
                return com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM;
            default:
                throw new IllegalStateException("Unknown detail level: " + detailLevel);
        }
    }

    static Collection<? extends com.pubnub.api.models.consumer.objects.PNSortKey<PNMembershipKey>> toInternal(Collection<PNSortKey> sort) {
        List<com.pubnub.api.models.consumer.objects.PNSortKey<PNMembershipKey>> list = new ArrayList<>(sort.size());
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
                default:
                    throw new IllegalStateException("Should never happen");
            }
            if (pnSortKey.getDir().equals(PNSortKey.Dir.ASC)) {
                list.add(new com.pubnub.api.models.consumer.objects.PNSortKey.PNAsc<>(key));
            } else {
                list.add(new com.pubnub.api.models.consumer.objects.PNSortKey.PNDesc<>(key));
            }
        }
        return list;
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
