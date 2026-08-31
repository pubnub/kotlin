package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.CreateUser;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncCreateUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreateUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.PNDataSyncCreateUserResult, PNDataSyncCreateUserResult>
        implements CreateUser {

    private final int classVersion;

    @Setter
    @Nullable
    private String userId;

    @Setter
    @Nullable
    private String className;

    @Setter
    @Nullable
    private PNDataSyncClassLevel classLevel;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    public CreateUserImpl(int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncCreateUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.PNDataSyncCreateUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncCreateUserResult(
                        result.getStatus(),
                        PNDataSyncUserConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.PNDataSyncCreateUserResult> createRemoteAction() {
        return pubnub.getDataSync().createUser(
                classVersion,
                userId,
                className,
                classLevel == null ? null : com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel.valueOf(classLevel.name()),
                status,
                payload
        );
    }
}
