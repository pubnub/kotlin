package com.pubnub.internal.java.endpoints.datasync.relationship;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.relationship.UpdateRelationship;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncRelationshipConverter;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class UpdateRelationshipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult, PNDataSyncUpdateRelationshipResult>
        implements UpdateRelationship {

    private final String relationshipId;
    private final List<PNJsonPatchOperation> operations;

    @Setter
    @Nullable
    private String ifMatch;

    public UpdateRelationshipImpl(String relationshipId, List<PNJsonPatchOperation> operations, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.relationshipId = relationshipId;
        this.operations = operations;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncUpdateRelationshipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncUpdateRelationshipResult(
                        result.getStatus(),
                        PNDataSyncRelationshipConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult> createRemoteAction() {
        final List<com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation> mapped =
                operations.stream()
                        .map(op -> new com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation(
                                op.getOp(),
                                op.getPath(),
                                op.getValue(),
                                op.getFrom()
                        ))
                        .collect(Collectors.toList());
        return pubnub.getDataSync().updateRelationship(relationshipId, mapped, ifMatch);
    }
}
