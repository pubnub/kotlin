package com.pubnub.internal.java.endpoints.datasync.user;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.user.UpdateUser;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncUpdateUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncUserConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class UpdateUserImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.user.DataSyncUpdateUserResult, DataSyncUpdateUserResult>
        implements UpdateUser {

    private final String userId;
    private final List<PNJsonPatchOperation> operations;

    @Setter
    @Nullable
    private String ifMatch;

    public UpdateUserImpl(String userId, List<PNJsonPatchOperation> operations, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.userId = userId;
        this.operations = operations;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<DataSyncUpdateUserResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.user.DataSyncUpdateUserResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new DataSyncUpdateUserResult(
                        result.getStatus(),
                        DataSyncUserConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.user.DataSyncUpdateUserResult> createRemoteAction() {
        final List<com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation> mapped =
                operations.stream()
                        .map(op -> new com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation(
                                op.getOp(),
                                op.getPath(),
                                op.getValue(),
                                op.getFrom()
                        ))
                        .collect(Collectors.toList());
        return pubnub.getDataSync().updateUser(userId, mapped, ifMatch);
    }
}
