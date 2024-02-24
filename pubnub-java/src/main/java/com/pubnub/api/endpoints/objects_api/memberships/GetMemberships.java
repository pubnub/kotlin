package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.membership.PNGetMembershipsResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMemberships extends DelegatingEndpoint<PNGetMembershipsResult> {

    private String uuid;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNChannelDetailsLevel includeChannel;

    public GetMemberships(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected ExtendedRemoteAction<PNGetMembershipsResult> createAction() {
        return new MappingRemoteAction<>(
                pubnub.getMemberships(
                        uuid,
                        limit,
                        page,
                        filter,
                        SetMemberships.toInternal(sort),
                        includeTotalCount,
                        includeCustom,
                        SetMemberships.toInternal(includeChannel)
                ),
                PNGetMembershipsResult::from);
    }
}


