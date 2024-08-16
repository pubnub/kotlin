package com.pubnub.internal.java.endpoints.objects_api.uuid;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.java.endpoints.objects_api.uuid.SetUUIDMetadata;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult;
import com.pubnub.internal.java.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetUUIDMetadataImpl extends DelegatingEndpoint<PNUUIDMetadataResult, PNSetUUIDMetadataResult> implements SetUUIDMetadata {
    @Setter
    private String uuid;
    @Setter
    private String name;
    @Setter
    private String externalId;
    @Setter
    private String profileUrl;
    @Setter
    private String email;

    private Map<String, Object> custom;
    @Setter
    private boolean includeCustom;
    @Setter
    private String type;
    @Setter
    private String status;

    public SetUUIDMetadataImpl(final PubNub pubnub) {
        super(pubnub);
    }

    @Override
    public SetUUIDMetadata custom(Map<String, Object> custom) {
        final HashMap<String, Object> customHashMap = new HashMap<>();
        if (custom != null) {
            customHashMap.putAll(custom);
        }
        this.custom = customHashMap;
        return this;
    }

    @Override
    @NotNull
    protected Endpoint<PNUUIDMetadataResult> createAction() {
        return pubnub.setUUIDMetadata(
                uuid,
                name,
                externalId,
                profileUrl,
                email,
                custom,
                includeCustom,
                type,
                status
        );
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNSetUUIDMetadataResult> mapResult(@NotNull ExtendedRemoteAction<PNUUIDMetadataResult> action) {
        return new MappingRemoteAction<>(
                action,
                result -> new PNSetUUIDMetadataResult(
                        result.getStatus(),
                        PNUUIDMetadata.from(result.getData())
                )
        );
    }
}
