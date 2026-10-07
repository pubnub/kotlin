package com.pubnub.internal.java.endpoints.datasync.relationship;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.relationship.RemoveRelationship;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(chain = true, fluent = true)
public class RemoveRelationshipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult, PNDataSyncRemoveRelationshipResult>
        implements RemoveRelationship {

    private final String relationshipId;

    @Setter
    @Nullable
    private String ifMatch;

    public RemoveRelationshipImpl(String relationshipId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.relationshipId = relationshipId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncRemoveRelationshipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncRemoveRelationshipResult(result.getStatus())
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult> createRemoteAction() {
        return pubnub.getDataSync().removeRelationship(relationshipId, ifMatch);
    }
}
