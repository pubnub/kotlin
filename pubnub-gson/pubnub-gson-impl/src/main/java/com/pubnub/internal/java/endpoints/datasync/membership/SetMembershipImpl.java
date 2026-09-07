package com.pubnub.internal.java.endpoints.datasync.membership;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.membership.SetMembership;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncMembershipConverter;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncSetMembershipResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetMembershipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncSetMembershipResult, PNDataSyncSetMembershipResult>
        implements SetMembership {

    private final String membershipId;
    private final int classVersion;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    @Setter
    @Nullable
    private String ifMatch;

    public SetMembershipImpl(String membershipId, int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.membershipId = membershipId;
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncSetMembershipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncSetMembershipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncSetMembershipResult(
                        result.getStatus(),
                        PNDataSyncMembershipConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncSetMembershipResult> createRemoteAction() {
        return pubnub.getDataSync().setMembership(
                membershipId,
                classVersion,
                status,
                payload,
                ifMatch
        );
    }
}