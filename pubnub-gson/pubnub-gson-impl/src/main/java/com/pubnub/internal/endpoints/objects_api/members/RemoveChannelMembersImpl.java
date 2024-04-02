package com.pubnub.internal.endpoints.objects_api.members;

import com.pubnub.api.endpoints.objects_api.members.RemoveChannelMembers;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.member.PNRemoveChannelMembersResult;
import com.pubnub.api.models.consumer.objects_api.member.PNUUID;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveChannelMembersImpl extends DelegatingEndpoint<PNRemoveChannelMembersResult> implements RemoveChannelMembers {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private final String channel;
    private final List<String> uuids;
    private Include.PNUUIDDetailsLevel includeUUID;

    public RemoveChannelMembersImpl(String channel, Collection<PNUUID> uuids, final PubNubCore pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
        this.uuids = new ArrayList<>(uuids.size());
        for (PNUUID uuid : uuids) {
            this.uuids.add(uuid.getUuid().getId());
        }
    }

    @Override
    protected ExtendedRemoteAction<PNRemoveChannelMembersResult> createAction() {
        return new MappingRemoteAction<>(pubnub.removeChannelMembers(
                channel,
                uuids,
                limit,
                page,
                filter,
                SetChannelMembersImpl.toInternal(sort),
                includeTotalCount,
                includeCustom,
                SetChannelMembersImpl.toInternal(includeUUID)
        ), PNRemoveChannelMembersResult::from);
    }

    @AllArgsConstructor
    public static class Builder implements RemoveChannelMembers.Builder {
        private final PubNubCore pubnubInstance;

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
