package com.pubnub.internal.java.endpoints.datasync.relationship;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.relationship.GetRelationships;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncPage;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncRelationshipConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class GetRelationshipsImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult, PNDataSyncGetRelationshipsResult>
        implements GetRelationships {

    private final String className;

    @Setter
    @Nullable
    private String entityAId;

    @Setter
    @Nullable
    private String entityBId;

    @Setter
    @Nullable
    private Integer classVersion;

    @Setter
    @Nullable
    private String filterFast;

    @Setter
    @Nullable
    private String filter;

    @Setter
    @Nullable
    private List<PNDataSyncSortField> sort;

    @Setter
    @Nullable
    private Integer limit;

    @Setter
    @Nullable
    private String cursor;

    public GetRelationshipsImpl(String className, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.className = className;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncGetRelationshipsResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult> action) {
        return new MappingRemoteAction<>(action, result -> {
            com.pubnub.api.models.consumer.datasync.PNDataSyncPage kNext = result.getNext();
            return new PNDataSyncGetRelationshipsResult(
                    result.getStatus(),
                    result.getData().stream().map(PNDataSyncRelationshipConverter::from).collect(Collectors.toList()),
                    new PNDataSyncPage(kNext.getCursor(), kNext.getHasNext(), kNext.getLimit())
            );
        });
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult> createRemoteAction() {
        final List<com.pubnub.api.models.consumer.datasync.PNDataSyncSortField> mappedSort =
                sort == null
                        ? Collections.emptyList()
                        : sort.stream()
                                .map(s -> new com.pubnub.api.models.consumer.datasync.PNDataSyncSortField(
                                        s.getProperty(),
                                        s.isAscending()
                                ))
                                .collect(Collectors.toList());
        return pubnub.getDataSync().getRelationships(
                className,
                entityAId,
                entityBId,
                classVersion,
                filterFast,
                filter,
                mappedSort,
                limit,
                cursor
        );
    }
}