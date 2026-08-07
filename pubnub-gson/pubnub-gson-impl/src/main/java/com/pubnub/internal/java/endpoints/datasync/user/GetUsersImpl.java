package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.GetUsers;
import com.pubnub.api.java.models.consumer.datasync.user.PNGetUsersResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class GetUsersImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.PNGetUsersResult, PNGetUsersResult>
        implements GetUsers {

    @Setter
    @Nullable
    private String entityClass;

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

    public GetUsersImpl(final PubNub pubnubInstance) {
        super(pubnubInstance);
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNGetUsersResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.PNGetUsersResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNGetUsersResult(
                        result.getStatus(),
                        result.getData().stream().map(PNUserConverter::from).collect(Collectors.toList()),
                        result.getNext(),
                        result.getHasNext(),
                        result.getLimit()
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.PNGetUsersResult> createRemoteAction() {
        return pubnub.getDataSync().getUsers(
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
