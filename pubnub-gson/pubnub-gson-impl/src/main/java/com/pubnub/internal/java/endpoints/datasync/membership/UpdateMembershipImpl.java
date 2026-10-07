package com.pubnub.internal.java.endpoints.datasync.membership;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.membership.UpdateMembership;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncMembershipConverter;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class UpdateMembershipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult, PNDataSyncUpdateMembershipResult>
        implements UpdateMembership {

    private final String membershipId;
    private final List<PNJsonPatchOperation> operations;

    @Setter
    @Nullable
    private String ifMatch;

    public UpdateMembershipImpl(String membershipId, List<PNJsonPatchOperation> operations, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.membershipId = membershipId;
        this.operations = operations;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncUpdateMembershipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncUpdateMembershipResult(
                        result.getStatus(),
                        PNDataSyncMembershipConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult> createRemoteAction() {
        final List<com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation> mapped =
                operations.stream()
                        .map(op -> new com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation(
                                op.getOp(),
                                op.getPath(),
                                op.getValue(),
                                op.getFrom()
                        ))
                        .collect(Collectors.toList());
        return pubnub.getDataSync().updateMembership(membershipId, mapped, ifMatch);
    }
}