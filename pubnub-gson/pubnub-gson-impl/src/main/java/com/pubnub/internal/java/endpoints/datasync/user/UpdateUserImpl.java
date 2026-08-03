package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.UpdateUser;
import com.pubnub.api.java.models.consumer.datasync.user.PNUpdateUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class UpdateUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.PNUpdateUserResult, PNUpdateUserResult>
        implements UpdateUser {

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

    public UpdateUserImpl(String userId, int entityClassVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.userId = userId;
        this.entityClassVersion = entityClassVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNUpdateUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.PNUpdateUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNUpdateUserResult(
                        result.getStatus(),
                        PNUserConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.PNUpdateUserResult> createRemoteAction() {
        return pubnub.getDataSync().getUser().update(
                userId,
                entityClassVersion,
                status,
                payload,
                ifMatch
        );
    }
}
