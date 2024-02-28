package com.pubnub.internal.endpoints.objects_api.members;

import com.pubnub.api.endpoints.objects_api.members.ManageChannelMembers;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.member.PNManageChannelMembersResult;
import com.pubnub.api.models.consumer.objects_api.member.PNUUID;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.objects.member.MemberInput;
import com.pubnub.internal.models.consumer.objects.member.PNMember;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class ManageChannelMembersImpl extends DelegatingEndpoint<PNManageChannelMembersResult> implements ManageChannelMembers {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private final String channel;
    private Include.PNUUIDDetailsLevel includeUUID;
    private final Collection<PNUUID> uuidsToSet;
    private final Collection<PNUUID> uuidsToRemove;

    public ManageChannelMembersImpl(String channel, Collection<PNUUID> uuidsToSet, Collection<PNUUID> uuidsToRemove, final CorePubNubClient pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
        this.uuidsToSet = uuidsToSet;
        this.uuidsToRemove = uuidsToRemove;
    }

    @Override
    protected ExtendedRemoteAction<PNManageChannelMembersResult> createAction() {
        List<String> toRemove = new ArrayList<String>(uuidsToRemove.size());
        for (PNUUID pnuuid : uuidsToRemove) {
            toRemove.add(pnuuid.getUuid().getId());
        }
        List<MemberInput> toSet = new ArrayList<MemberInput>(uuidsToSet.size());
        for (PNUUID pnuuid : uuidsToSet) {
            toSet.add(new PNMember.Partial(
                    pnuuid.getUuid().getId(),
                    (pnuuid instanceof PNUUID.UUIDWithCustom) ? ((PNUUID.UUIDWithCustom) pnuuid).getCustom() : null,
                    pnuuid.getStatus()
            ));
        }

        return new MappingRemoteAction<>(pubnub.manageChannelMembers(
                channel,
                toSet,
                toRemove,
                limit,
                page,
                filter,
                SetChannelMembersImpl.toInternal(sort),
                includeTotalCount,
                includeCustom,
                SetChannelMembersImpl.toInternal(includeUUID)
        ), PNManageChannelMembersResult::from);
    }

    @AllArgsConstructor
    public static class Builder implements ManageChannelMembers.Builder {
        private final CorePubNubClient pubnubInstance;

        @Override
        public ObjectsBuilderSteps.RemoveOrSetStep<ManageChannelMembers, PNUUID> channel(final String channel) {
            return new ObjectsBuilderSteps.RemoveOrSetStep<ManageChannelMembers, PNUUID>() {
                @Override
                public RemoveStep<ManageChannelMembers, PNUUID> set(final Collection<PNUUID> uuidsToSet) {
                    return new RemoveStep<ManageChannelMembers, PNUUID>() {
                        @Override
                        public ManageChannelMembers remove(final Collection<PNUUID> uuidsToRemove) {
                            return new ManageChannelMembersImpl(
                                    channel,
                                    uuidsToSet,
                                    uuidsToRemove,
                                    pubnubInstance
                            );
                        }
                    };
                }

                @Override
                public SetStep<ManageChannelMembers, PNUUID> remove(final Collection<PNUUID> uuidsToRemove) {
                    return new SetStep<ManageChannelMembers, PNUUID>() {
                        @Override
                        public ManageChannelMembers set(final Collection<PNUUID> uuidsToSet) {
                            return new ManageChannelMembersImpl(
                                    channel,
                                    uuidsToSet,
                                    uuidsToRemove,
                                    pubnubInstance
                            );
                        }
                    };
                }
            };
        }
    }
}
