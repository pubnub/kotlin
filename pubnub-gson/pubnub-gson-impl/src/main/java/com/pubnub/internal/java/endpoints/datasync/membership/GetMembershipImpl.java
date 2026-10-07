package com.pubnub.internal.java.endpoints.datasync.membership;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.membership.GetMembership;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncGetMembershipResult;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncMembershipConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

public class GetMembershipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipResult, PNDataSyncGetMembershipResult>
        implements GetMembership {

    private final String membershipId;

    public GetMembershipImpl(String membershipId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.membershipId = membershipId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncGetMembershipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncGetMembershipResult(
                        result.getStatus(),
                        PNDataSyncMembershipConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipResult> createRemoteAction() {
        return pubnub.getDataSync().getMembership(membershipId);
    }
}