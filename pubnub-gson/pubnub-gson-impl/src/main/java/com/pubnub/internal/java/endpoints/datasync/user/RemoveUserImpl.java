package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.RemoveUser;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncRemoveUserResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(chain = true, fluent = true)
public class RemoveUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.PNDataSyncRemoveUserResult, PNDataSyncRemoveUserResult>
        implements RemoveUser {

    private final String userId;

    @Setter
    @Nullable
    private String ifMatch;

    public RemoveUserImpl(String userId, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.userId = userId;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncRemoveUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.PNDataSyncRemoveUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncRemoveUserResult(result.getStatus())
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.PNDataSyncRemoveUserResult> createRemoteAction() {
        return pubnub.getDataSync().removeUser(userId, ifMatch);
    }
}
