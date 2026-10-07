package com.pubnub.internal.java.endpoints.datasync.relationship;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.relationship.GetRelationship;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncGetRelationshipResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncRelationshipConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

public class GetRelationshipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipResult, PNDataSyncGetRelationshipResult>
        implements GetRelationship {

    private final String relationshipId;

    public GetRelationshipImpl(String relationshipId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.relationshipId = relationshipId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncGetRelationshipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncGetRelationshipResult(
                        result.getStatus(),
                        PNDataSyncRelationshipConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipResult> createRemoteAction() {
        return pubnub.getDataSync().getRelationship(relationshipId);
    }
}