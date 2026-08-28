package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.GetUser;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncGetUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

public class GetUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.DataSyncGetUserResult, DataSyncGetUserResult>
        implements GetUser {

    private final String userId;

    public GetUserImpl(String userId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.userId = userId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<DataSyncGetUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.DataSyncGetUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new DataSyncGetUserResult(
                        result.getStatus(),
                        DataSyncUserConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.DataSyncGetUserResult> createRemoteAction() {
        return pubnub.getDataSync().getUser(userId);
    }
}