package com.pubnub.internal.java.endpoints.objects_api.members;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.members.RemoveChannelMembers;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.PNRemoveChannelMembersResult;
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

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveChannelMembersImpl extends DelegatingEndpoint<PNMemberArrayResult, PNRemoveChannelMembersResult> implements RemoveChannelMembers {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private boolean includeType;
    private final String channel;
    private final List<String> uuids;
    private Include.PNUUIDDetailsLevel includeUUID;

    public RemoveChannelMembersImpl(String channel, Collection<PNUUID> uuids, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
        this.uuids = new ArrayList<>(uuids.size());
        for (PNUUID uuid : uuids) {
            this.uuids.add(uuid.getUuid().getId());
        }
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNRemoveChannelMembersResult> mapResult(@NotNull ExtendedRemoteAction<PNMemberArrayResult> action) {
        return new MappingRemoteAction<>(action, PNRemoveChannelMembersResult::from);
    }

    @Override
    @NotNull
    protected Endpoint<PNMemberArrayResult> createAction() {
        return pubnub.removeChannelMembers(
                channel,
                uuids,
                limit,
                page,
                filter,
                SetChannelMembersImpl.toInternal(sort),
                includeTotalCount,
                includeCustom,
                SetChannelMembersImpl.toInternal(includeUUID),
                includeType
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
