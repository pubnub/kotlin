package com.pubnub.internal.endpoints.objects_api.members;

import com.pubnub.api.endpoints.objects_api.members.SetChannelMembers;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.member.PNSetChannelMembersResult;
import com.pubnub.api.models.consumer.objects_api.member.PNUUID;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.objects.PNMemberKey;
import com.pubnub.internal.models.consumer.objects.member.MemberInput;
import com.pubnub.internal.models.consumer.objects.member.PNMember;
import com.pubnub.internal.models.consumer.objects.member.PNUUIDDetailsLevel;
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
public class SetChannelMembersImpl extends DelegatingEndpoint<PNSetChannelMembersResult> implements SetChannelMembers {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNUUIDDetailsLevel includeUUID;
    private final String channel;
    private final Collection<PNUUID> uuids;

    public SetChannelMembersImpl(final PubNubCore pubnubInstance, String channel, Collection<PNUUID> uuids) {
        super(pubnubInstance);
        this.channel = channel;
        this.uuids = uuids;
    }

    @Override
    protected ExtendedRemoteAction<PNSetChannelMembersResult> createAction() {
        List<MemberInput> memberInputs = new ArrayList<>(uuids.size());
        for (PNUUID uuid : uuids) {
            memberInputs.add(new PNMember.Partial(
                    uuid.getUuid().getId(),
                    (uuid instanceof PNUUID.UUIDWithCustom)
                            ? ((PNUUID.UUIDWithCustom) uuid).getCustom()
                            : null,
                    uuid.getStatus()));
        }
        return new MappingRemoteAction<>(
                pubnub.setChannelMembers(
                        channel,
                        memberInputs,
                        limit,
                        page,
                        filter,
                        toInternal(sort),
                        includeTotalCount,
                        includeCustom,
                        toInternal(includeUUID)
                ),
                PNSetChannelMembersResult::from);
    }

    static Collection<? extends com.pubnub.internal.models.consumer.objects.PNSortKey<PNMemberKey>> toInternal(Collection<PNSortKey> sort) {
        List<com.pubnub.internal.models.consumer.objects.PNSortKey<PNMemberKey>> list = new ArrayList<>(sort.size());
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
                list.add(new com.pubnub.internal.models.consumer.objects.PNSortKey.PNAsc<>(key));
            } else {
                list.add(new com.pubnub.internal.models.consumer.objects.PNSortKey.PNDesc<>(key));
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

    @AllArgsConstructor
    public static class Builder implements SetChannelMembers.Builder {
        private final PubNubCore pubnubInstance;

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
}
