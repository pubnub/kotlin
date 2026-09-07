package com.pubnub.internal.java.endpoints.datasync.membership;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.membership.RemoveMembership;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(chain = true, fluent = true)
public class RemoveMembershipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult, PNDataSyncRemoveMembershipResult>
        implements RemoveMembership {

    private final String membershipId;

    @Setter
    @Nullable
    private String ifMatch;

    public RemoveMembershipImpl(String membershipId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.membershipId = membershipId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncRemoveMembershipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncRemoveMembershipResult(result.getStatus())
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult> createRemoteAction() {
        return pubnub.getDataSync().removeMembership(membershipId, ifMatch);
    }
}