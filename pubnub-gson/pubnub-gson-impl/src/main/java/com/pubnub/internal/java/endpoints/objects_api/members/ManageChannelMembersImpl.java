package com.pubnub.internal.java.endpoints.objects_api.members;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.members.ManageChannelMembers;
import com.pubnub.api.java.endpoints.objects_api.members.ManageChannelMembersBuilder;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNManageChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNManageChannelMembersResultConverter;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUUID;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUser;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.member.MemberInput;
import com.pubnub.api.models.consumer.objects.member.PNMember;
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.pubnub.internal.java.endpoints.objects_api.members.SetChannelMembersImpl.getMemberInclude;

@Setter
@Accessors(chain = true, fluent = true)
public class ManageChannelMembersImpl extends DelegatingEndpoint<PNMemberArrayResult, PNManageChannelMembersResult> implements ManageChannelMembers, ManageChannelMembersBuilder {

    private final String channel;
    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount; // deprecated
    private boolean includeCustom; // deprecated
    private boolean includeType; // deprecated
    private Include.PNUUIDDetailsLevel includeUUID; // deprecated
    private Collection<PNUUID> uuidsToSet; // deprecated
    private Collection<PNUUID> uuidsToRemove; // deprecated
    private Collection<PNUser> usersToSet;
    private List<String> usersIdsToRemove;
    private MemberInclude include;

    @Deprecated
    public ManageChannelMembersImpl(String channel, Collection<PNUUID> uuidsToSet, Collection<PNUUID> uuidsToRemove, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
        this.uuidsToSet = uuidsToSet;
        this.uuidsToRemove = uuidsToRemove;
    }

    public ManageChannelMembersImpl(final PubNub pubnubInstance, String channel, Collection<PNUser> usersToSet, Collection<String> usersIdsToRemove) {
        super(pubnubInstance);
        this.channel = channel;
        this.usersToSet = usersToSet;
        this.usersIdsToRemove = new ArrayList<>(usersIdsToRemove);
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNManageChannelMembersResult> mapResult(@NotNull ExtendedRemoteAction<PNMemberArrayResult> action) {
        return new MappingRemoteAction<>(action, PNManageChannelMembersResultConverter::from);
    }

    @Override
    @NotNull
    protected Endpoint<PNMemberArrayResult> createRemoteAction() {
        List<MemberInput> toSet;
        List<String> toRemove;

        // new API use
        if (usersToSet != null || usersIdsToRemove != null) {
            toSet = createMemberInputFromUserToSet();
            toRemove = usersIdsToRemove;
        } else { // old API used
            toSet = createMemberInputFromUUIDToSet();
            toRemove = createUserIdsFromUUIDToRemove();
        }

        return pubnub.manageChannelMembers(
                channel,
                toSet,
                toRemove,
                limit,
                page,
                filter,
                SetChannelMembersImpl.toInternal(sort),
                getMemberInclude(include, includeUUID, includeTotalCount, includeCustom, includeType)
        );
    }

    public static class Builder implements ManageChannelMembers.Builder {
        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

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

    private List<MemberInput> createMemberInputFromUserToSet() {
        if (usersToSet == null) {
            return Collections.emptyList();
        }
        List<MemberInput> memberInputs = new ArrayList<>();
        for (PNUser user : usersToSet) {
            memberInputs.add(new PNMember.Partial(
                    user.getUserId(),
                    user.getCustom(), // despite IDE error this works ¯\_(ツ)_/¯
                    user.getStatus(),
                    user.getType()
            ));
        }
        return memberInputs;
    }
    private List<MemberInput> createMemberInputFromUUIDToSet() {
        if (uuidsToSet == null) {
            return Collections.emptyList();
        }
        List<MemberInput> memberInputs = new ArrayList<>();
        for (PNUUID uuid : uuidsToSet) {
            memberInputs.add(new PNMember.Partial(
                    uuid.getUuid().getId(),
                    (uuid instanceof PNUUID.UUIDWithCustom) ? ((PNUUID.UUIDWithCustom) uuid).getCustom() : null, // despite IDE error this works ¯\_(ツ)_/¯
                    uuid.getStatus(),
                    null
            ));
        }
        return memberInputs;
    }

    private List<String> createUserIdsFromUUIDToRemove() {
        if (uuidsToRemove == null) {
            return Collections.emptyList();
        }
        List<String> toRemove = new ArrayList<>();
        for (PNUUID uuid : uuidsToRemove) {
            toRemove.add(uuid.getUuid().getId());
        }
        return toRemove;
    }
}
