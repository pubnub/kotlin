package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.SetUser;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncSetUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.DataSyncSetUserResult, DataSyncSetUserResult>
        implements SetUser {

    private final String userId;
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

    public SetUserImpl(String userId, int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.userId = userId;
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<DataSyncSetUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.DataSyncSetUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new DataSyncSetUserResult(
                        result.getStatus(),
                        DataSyncUserConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.DataSyncSetUserResult> createRemoteAction() {
        return pubnub.getDataSync().setUser(
                userId,
                classVersion,
                status,
                payload,
                ifMatch
        );
    }
}
