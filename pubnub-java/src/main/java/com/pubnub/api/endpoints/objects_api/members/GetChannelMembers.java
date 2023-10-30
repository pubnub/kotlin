package com.pubnub.api.endpoints.objects_api.members;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.member.PNGetChannelMembersResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class GetChannelMembers extends Endpoint<PNGetChannelMembersResult> {

    private final String channel;
    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNUUIDDetailsLevel includeUUID;

    public GetChannelMembers(String channel, final PubNubImpl pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    @Override
    protected ExtendedRemoteAction<PNGetChannelMembersResult> createAction() {
        return new MappingRemoteAction<>(pubnub.getChannelMembers(
                channel,
                limit,
                page,
                filter,
                SetChannelMembers.toInternal(sort),
                includeTotalCount,
                includeCustom,
                SetChannelMembers.toInternal(includeUUID)
        ), PNGetChannelMembersResult::from);
    }

    public static Builder builder(final PubNubImpl pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements BuilderSteps.ChannelStep<GetChannelMembers> {
        private final PubNubImpl pubnubInstance;

        @Override
        public GetChannelMembers channel(final String channel) {
            return new GetChannelMembers(channel, pubnubInstance);
        }
    }
}
