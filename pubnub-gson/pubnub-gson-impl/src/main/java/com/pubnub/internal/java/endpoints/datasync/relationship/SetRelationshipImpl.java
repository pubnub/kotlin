package com.pubnub.internal.java.endpoints.datasync.relationship;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.relationship.SetRelationship;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncRelationshipConverter;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetRelationshipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult, PNDataSyncSetRelationshipResult>
        implements SetRelationship {

    private final String relationshipId;
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

    public SetRelationshipImpl(String relationshipId, int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.relationshipId = relationshipId;
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncSetRelationshipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncSetRelationshipResult(
                        result.getStatus(),
                        PNDataSyncRelationshipConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult> createRemoteAction() {
        return pubnub.getDataSync().setRelationship(
                relationshipId,
                classVersion,
                status,
                payload,
                ifMatch
        );
    }
}
