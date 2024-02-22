package com.pubnub.api.endpoints.objects_api.members;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.member.PNRemoveChannelMembersResult;
import com.pubnub.api.models.consumer.objects_api.member.PNUUID;
import com.pubnub.internal.InternalPubNubClient;
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
public class RemoveChannelMembers extends Endpoint<PNRemoveChannelMembersResult> {

    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private final String channel;
    private final List<String> uuids;
    private Include.PNUUIDDetailsLevel includeUUID;

    public RemoveChannelMembers(String channel, Collection<PNUUID> uuids, final InternalPubNubClient pubnubInstance) {
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
                SetChannelMembers.toInternal(sort),
                includeTotalCount,
                includeCustom,
                SetChannelMembers.toInternal(includeUUID)
        ), PNRemoveChannelMembersResult::from);
    }

    public static Builder builder(final InternalPubNubClient pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements BuilderSteps.ChannelStep<ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers>> {
        private final InternalPubNubClient pubnubInstance;

        @Override
        public ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers> channel(final String channel) {
            return new ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers>() {
                @Override
                public RemoveChannelMembers uuids(@NotNull final Collection<PNUUID> uuids) {
                    return new RemoveChannelMembers(channel, uuids, pubnubInstance);
                }
            };
        }
    }
}
