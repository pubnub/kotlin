package com.pubnub.internal.endpoints.objects_api.members;

import com.pubnub.api.endpoints.objects_api.members.GetChannelMembers;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.member.PNGetChannelMembersResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class GetChannelMembersImpl extends DelegatingEndpoint<PNMemberArrayResult, PNGetChannelMembersResult> implements GetChannelMembers {

    private final String channel;
    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNUUIDDetailsLevel includeUUID;

    public GetChannelMembersImpl(String channel, final PubNubCore pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetChannelMembersResult> mapResult(@NotNull ExtendedRemoteAction<PNMemberArrayResult> action) {
        return new MappingRemoteAction<>(action, PNGetChannelMembersResult::from);
    }

    @Override
    protected EndpointInterface<PNMemberArrayResult> createAction() {
        return pubnub.getChannelMembers(
                channel,
                limit,
                page,
                filter,
                SetChannelMembersImpl.toInternal(sort),
                includeTotalCount,
                includeCustom,
                SetChannelMembersImpl.toInternal(includeUUID)
        );
    }

    @AllArgsConstructor
    public static class Builder implements GetChannelMembers.Builder {
        private final PubNubCore pubnubInstance;

        @Override
        public GetChannelMembers channel(final String channel) {
            return new GetChannelMembersImpl(channel, pubnubInstance);
        }
    }
}
