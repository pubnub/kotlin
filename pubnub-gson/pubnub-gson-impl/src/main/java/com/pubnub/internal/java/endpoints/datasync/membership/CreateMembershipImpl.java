package com.pubnub.internal.java.endpoints.datasync.membership;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.datasync.membership.CreateMembership;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncMembershipConverter;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Accessors(chain = true, fluent = true)
public class CreateMembershipImpl
        extends DelegatingEndpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult, PNDataSyncCreateMembershipResult>
        implements CreateMembership {

    private final String channelId;
    private final String userId;
    private final int classVersion;

    @Setter
    @Nullable
    private String membershipId;

    @Setter
    @Nullable
    private String status;

    @Setter
    @Nullable
    private Map<String, Object> payload;

    public CreateMembershipImpl(String channelId, String userId, int classVersion, final PubNub pubnubInstance) {
        super(pubnubInstance);
        this.channelId = channelId;
        this.userId = userId;
        this.classVersion = classVersion;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNDataSyncCreateMembershipResult> mapResult(
            @NotNull ExtendedRemoteAction<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult> action) {
        return new MappingRemoteAction<>(action, result ->
                new PNDataSyncCreateMembershipResult(
                        result.getStatus(),
                        PNDataSyncMembershipConverter.from(result.getData())
                )
        );
    }

    @Override
    @NotNull
    protected Endpoint<com.pubnub.api.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult> createRemoteAction() {
        return pubnub.getDataSync().createMembership(
                channelId,
                userId,
                classVersion,
                membershipId,
                status,
                payload
        );
    }
}