package com.pubnub.internal.java.endpoints.objects_api.members;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.members.RemoveChannelMembers;
import com.pubnub.api.java.endpoints.objects_api.members.RemoveChannelMembersBuilder;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNRemoveChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNRemoveChannelMembersResultConverter;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUUID;
import com.pubnub.api.models.consumer.objects.PNPage;
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
public class RemoveChannelMembersImpl extends DelegatingEndpoint<PNMemberArrayResult, PNRemoveChannelMembersResult> implements RemoveChannelMembers, RemoveChannelMembersBuilder {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount; // deprecated
    private boolean includeCustom; // deprecated
    private boolean includeType; // deprecated
    private final String channel;
    private final List<String> userIds;
    private Include.PNUUIDDetailsLevel includeUUID; // deprecated
    private MemberInclude include;

    @Deprecated
    private RemoveChannelMembersImpl(String channel, Collection<PNUUID> uuids, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
        this.userIds = new ArrayList<>(uuids.size());
        for (PNUUID uuid : uuids) {
            this.userIds.add(uuid.getUuid().getId());
        }
    }

    public RemoveChannelMembersImpl(final PubNub pubnubInstance, String channel, Collection<String> userIds) {
        super(pubnubInstance);
        this.channel = channel;
        this.userIds = new ArrayList<>(userIds);
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNRemoveChannelMembersResult> mapResult(@NotNull ExtendedRemoteAction<PNMemberArrayResult> action) {
        return new MappingRemoteAction<>(action, PNRemoveChannelMembersResultConverter::from);
    }

    @Override
    @NotNull
    protected Endpoint<PNMemberArrayResult> createRemoteAction() {
        return pubnub.removeChannelMembers(
                channel,
                userIds,
                limit,
                page,
                filter,
                SetChannelMembersImpl.toInternal(sort),
                getMemberInclude(include, includeUUID, includeTotalCount, includeCustom, includeType)
        );
    }

    public static class Builder implements RemoveChannelMembers.Builder {
        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers> channel(final String channel) {
            return new ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers>() {
                @Override
                public RemoveChannelMembers uuids(@NotNull final Collection<PNUUID> uuids) {
                    return new RemoveChannelMembersImpl(channel, uuids, pubnubInstance);
                }
            };
        }
    }
}
