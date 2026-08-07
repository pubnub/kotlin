package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.CreateUser;
import com.pubnub.api.java.models.consumer.datasync.user.PNCreateUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreateUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.PNCreateUserResult, PNCreateUserResult>
        implements CreateUser {

    private final int entityClassVersion;

    @Setter
    @Nullable
    private String userId;

    @Setter
    @Nullable
    private String entityClass;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    public CreateUserImpl(int entityClassVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.entityClassVersion = entityClassVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNCreateUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.PNCreateUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNCreateUserResult(
                        result.getStatus(),
                        PNUserConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.PNCreateUserResult> createRemoteAction() {
        return pubnub.getDataSync().createUser(
                entityClassVersion,
                userId,
                entityClass,
                status,
                payload
        );
    }
}
