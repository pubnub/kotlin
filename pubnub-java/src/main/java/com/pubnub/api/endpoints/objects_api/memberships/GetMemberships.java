package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include;
import com.pubnub.api.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.PNPage;
import com.pubnub.api.models.consumer.objects_api.membership.PNGetMembershipsResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Collections;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMemberships extends ValidatingEndpoint<PNGetMembershipsResult> {

    private String uuid;
    private Integer limit;
    private PNPage page;
    private String filter;
    private Collection<PNSortKey> sort = Collections.emptyList();
    private boolean includeTotalCount;
    private boolean includeCustom;
    private Include.PNChannelDetailsLevel includeChannel;

    public GetMemberships(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNGetMembershipsResult> createAction() {
        return new MappingEndpoint<>(
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


