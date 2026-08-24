package com.pubnub.internal.java.endpoints.datasync.entity;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntities;
import com.pubnub.api.java.models.consumer.datasync.entity.PNEntityConverter;
import com.pubnub.api.java.models.consumer.datasync.entity.PNGetEntitiesResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class GetEntitiesImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.entity.PNGetEntitiesResult, PNGetEntitiesResult>
        implements GetEntities {

    private final String entityClass;

    @Setter
    @Nullable
    private Integer entityClassVersion;

    @Setter
    @Nullable
    private String entityClassLevel;

    @Setter
    @Nullable
    private String filter;

    @Setter
    @Nullable
    private String filterAdvanced;

    @Setter
    @Nullable
    private String sort;

    @Setter
    @Nullable
    private Integer limit;

    @Setter
    @Nullable
    private String cursor;

    public GetEntitiesImpl(String entityClass, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityClass = entityClass;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetEntitiesResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.entity.PNGetEntitiesResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNGetEntitiesResult(
                        result.getStatus(),
                        result.getData().stream().map(PNEntityConverter::from).collect(Collectors.toList()),
                        result.getNext(),
                        result.getHasNext(),
                        result.getLimit()
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.entity.PNGetEntitiesResult> createRemoteAction() {
        return pubnub.getDataSync().getEntities(
                entityClass,
                entityClassVersion,
                entityClassLevel,
                filter,
                filterAdvanced,
                sort,
                limit,
                cursor
        );
    }
}