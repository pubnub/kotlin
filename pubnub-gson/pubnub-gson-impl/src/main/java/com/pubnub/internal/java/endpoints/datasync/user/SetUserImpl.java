package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.SetUser;
import com.pubnub.api.java.models.consumer.datasync.user.PNSetUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.PNSetUserResult, PNSetUserResult>
        implements SetUser {

    private final String userId;
    private final int entityClassVersion;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    @Setter
    @Nullable
    private String ifMatch;

    public SetUserImpl(String userId, int entityClassVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.userId = userId;
        this.entityClassVersion = entityClassVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNSetUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.PNSetUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNSetUserResult(
                        result.getStatus(),
                        PNUserConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.PNSetUserResult> createRemoteAction() {
        return pubnub.getDataSync().setUser(
                userId,
                entityClassVersion,
                status,
                payload,
                ifMatch
        );
    }
}
