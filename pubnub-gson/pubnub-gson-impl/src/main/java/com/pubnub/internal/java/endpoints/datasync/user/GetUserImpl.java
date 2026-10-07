package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.GetUser;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncGetUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

public class GetUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.PNDataSyncGetUserResult, PNDataSyncGetUserResult>
        implements GetUser {

    private final String userId;

    public GetUserImpl(String userId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.userId = userId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncGetUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.PNDataSyncGetUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncGetUserResult(
                        result.getStatus(),
                        PNDataSyncUserConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.PNDataSyncGetUserResult> createRemoteAction() {
        return pubnub.getDataSync().getUser(userId);
    }
}