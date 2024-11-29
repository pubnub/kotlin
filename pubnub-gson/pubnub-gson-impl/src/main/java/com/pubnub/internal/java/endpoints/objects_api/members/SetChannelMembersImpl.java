package com.pubnub.internal.java.endpoints.objects_api.members;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.members.SetChannelMembers;
import com.pubnub.api.java.endpoints.objects_api.members.SetChannelMembersBuilder;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNSetChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNSetChannelMembersResultConverter;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUUID;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUser;
import com.pubnub.api.models.consumer.objects.PNMemberKey;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.member.MemberInput;
import com.pubnub.api.models.consumer.objects.member.PNMember;
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult;
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel;
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
public class SetChannelMembersImpl extends DelegatingEndpoint<PNMemberArrayResult, PNSetChannelMembersResult> implements SetChannelMembers, SetChannelMembersBuilder {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;  // deprecated
    private boolean includeCustom;  // deprecated
    private boolean includeType;  // deprecated
    private Include.PNUUIDDetailsLevel includeUUID;  // deprecated
    private final String channel;
    private Collection<PNUUID> uuids; // deprecated
    private Collection<PNUser> users;
    private MemberInclude include;

    @Deprecated
    private SetChannelMembersImpl(final PubNub pubnubInstance, String channel, Collection<PNUUID> uuids) {
        super(pubnubInstance);
        this.channel = channel;
        this.uuids = uuids;
    }

    public SetChannelMembersImpl(String channel, Collection<PNUser> users, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
        this.users = users;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNSetChannelMembersResult> mapResult(@NotNull ExtendedRemoteAction<PNMemberArrayResult> action) {
        MappingRemoteAction<PNMemberArrayResult, PNSetChannelMembersResult> pnMemberArrayResultPNSetChannelMembersResultMappingRemoteAction = new MappingRemoteAction<>(action, PNSetChannelMembersResultConverter::from);
        return pnMemberArrayResultPNSetChannelMembersResultMappingRemoteAction;
    }

    @Override
    @NotNull
    protected Endpoint<PNMemberArrayResult> createRemoteAction() {
        return pubnub.setChannelMembers(
                channel,
                createMemberInputs(),
                limit,
                page,
                filter,
                toInternal(sort),
                getMemberInclude(include, includeUUID, includeTotalCount, includeCustom, includeType)
        );
    }

    static Collection<? extends com.pubnub.api.models.consumer.objects.PNSortKey<PNMemberKey>> toInternal(Collection<PNSortKey> sort) {
        List<com.pubnub.api.models.consumer.objects.PNSortKey<PNMemberKey>> list = new ArrayList<>(sort.size());
        for (PNSortKey pnSortKey : sort) {
            PNMemberKey key = null;
            switch (pnSortKey.getKey()) {
                case ID:
                    key = PNMemberKey.UUID_ID;
                    break;
                case NAME:
                    key = PNMemberKey.UUID_NAME;
                    break;
                case UPDATED:
                    key = PNMemberKey.UUID_UPDATED;
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

    @Nullable
    static PNUUIDDetailsLevel toInternal(Include.PNUUIDDetailsLevel detailLevel) {
        if (detailLevel == null) {
            return null;
        }
        switch (detailLevel) {
            case UUID:
                return PNUUIDDetailsLevel.UUID;
            case UUID_WITH_CUSTOM:
                return PNUUIDDetailsLevel.UUID_WITH_CUSTOM;
            default:
                throw new IllegalStateException("Unknown detail level: " + detailLevel);
        }
    }

    public static class Builder implements SetChannelMembers.Builder {
        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public ObjectsBuilderSteps.UUIDsStep<SetChannelMembers> channel(final String channel) {
            return new ObjectsBuilderSteps.UUIDsStep<SetChannelMembers>() {
                @Override
                public SetChannelMembers uuids(@NotNull final Collection<PNUUID> uuids) {
                    return new SetChannelMembersImpl(pubnubInstance, channel, uuids);
                }
            };
        }
    }


    private List<MemberInput> createMemberInputs() {
        List<MemberInput> memberInputs = new ArrayList<>();
        if (users != null) {
            for (PNUser user: users) {
                memberInputs.add(new PNMember.Partial(
                        user.getUserId(),
                        user.getCustom(), // despite IDE error this works ¯\_(ツ)_/¯
                        user.getStatus(),
                        user.getType()
                ));
            }
        } else if (uuids != null) {
            for (PNUUID uuid : uuids) {
                memberInputs.add(new PNMember.Partial(
                        uuid.getUuid().getId(),
                        (uuid instanceof PNUUID.UUIDWithCustom) ? ((PNUUID.UUIDWithCustom) uuid).getCustom() : null,
                        uuid.getStatus(),
                        null
                ));
            }
        }
        return memberInputs;
    }

    static MemberInclude getMemberInclude(
            MemberInclude include,
            Include.PNUUIDDetailsLevel includeUser,
            boolean includeTotalCount,
            boolean includeCustom,
            boolean includeType
    ) {
        if (include != null) {
            return include;
        } else {
            // if deprecated setChannelMembership API use
            if (includeUser == Include.PNUUIDDetailsLevel.UUID) {
                return MemberInclude.builder()
                        .includeTotalCount(includeTotalCount)
                        .includeCustom(includeCustom)
                        .includeUserType(includeType)
                        .includeStatus(true)
                        .includeUser(true)
                        .build();


            } else if (includeUser == Include.PNUUIDDetailsLevel.UUID_WITH_CUSTOM) {
                return MemberInclude.builder()
                        .includeTotalCount(includeTotalCount)
                        .includeCustom(includeCustom)
                        .includeUserType(includeType)
                        .includeStatus(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build();
            } else {
                return MemberInclude.builder()
                        .includeTotalCount(includeTotalCount)
                        .includeCustom(includeCustom)
                        .includeUserType(includeType)
                        .build();
            }
        }
    }
}
