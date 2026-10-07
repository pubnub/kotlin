package com.pubnub.internal.java.endpoints.datasync.relationship;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.relationship.CreateRelationship;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncRelationshipConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreateRelationshipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult, PNDataSyncCreateRelationshipResult>
        implements CreateRelationship {

    private final String entityAId;
    private final String entityBId;
    private final String className;
    private final int classVersion;

    @Setter
    @Nullable
    private String relationshipId;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    public CreateRelationshipImpl(String entityAId, String entityBId, String className, int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityAId = entityAId;
        this.entityBId = entityBId;
        this.className = className;
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncCreateRelationshipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncCreateRelationshipResult(
                        result.getStatus(),
                        PNDataSyncRelationshipConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult> createRemoteAction() {
        return pubnub.getDataSync().createRelationship(
                entityAId,
                entityBId,
                className,
                classVersion,
                relationshipId,
                status,
                payload
        );
    }
}