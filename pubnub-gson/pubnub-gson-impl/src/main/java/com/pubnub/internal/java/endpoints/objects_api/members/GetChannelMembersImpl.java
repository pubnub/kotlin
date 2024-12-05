package com.pubnub.internal.java.endpoints.objects_api.members;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.members.GetChannelMembers;
import com.pubnub.api.java.endpoints.objects_api.members.GetChannelMembersBuilder;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNGetChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNGetChannelMembersResultConverter;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

import static com.pubnub.internal.java.endpoints.objects_api.members.SetChannelMembersImpl.getMemberInclude;

@Setter
@Accessors(chain = true, fluent = true)
public class GetChannelMembersImpl extends DelegatingEndpoint<PNMemberArrayResult, PNGetChannelMembersResult> implements GetChannelMembers, GetChannelMembersBuilder {

    private final String channel;
    private Integer limit = null;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount; // deprecated
    private boolean includeCustom; // deprecated
    private boolean includeType; // deprecated
    private Include.PNUUIDDetailsLevel includeUUID; // deprecated
    private MemberInclude include;

    public GetChannelMembersImpl(String channel, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetChannelMembersResult> mapResult(@NotNull ExtendedRemoteAction<PNMemberArrayResult> action) {
        return new MappingRemoteAction<>(action, PNGetChannelMembersResultConverter::from);
    }

    @Override
    protected Endpoint<PNMemberArrayResult> createRemoteAction() {
        return pubnub.getChannelMembers(
                channel,
                limit,
                page,
                filter,
                SetChannelMembersImpl.toInternal(sort),
                getMemberInclude(include, includeUUID, includeTotalCount, includeCustom, includeType)
        );
    }

    public static class Builder implements GetChannelMembers.Builder {
        private final PubNub pubnubInstance;

        public Builder(PubNub pubnubInstance) {
            this.pubnubInstance = pubnubInstance;
        }

        @Override
        public GetChannelMembers channel(final String channel) {
            return new GetChannelMembersImpl(channel, pubnubInstance);
        }
    }
}
