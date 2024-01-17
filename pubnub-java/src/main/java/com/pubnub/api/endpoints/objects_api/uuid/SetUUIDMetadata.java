package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingEndpoint;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Setter
@Accessors(chain = true, fluent = true)
public class SetUUIDMetadata extends ValidatingEndpoint<PNSetUUIDMetadataResult> {
    private String uuid;
    private String name;
    private String externalId;
    private String profileUrl;
    private String email;
    private Map<String, Object> custom;
    private boolean includeCustom;
    private String type;
    private String status;

    public SetUUIDMetadata(final com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    public SetUUIDMetadata custom(Map<String, Object> custom) {
        final HashMap<String, Object> customHashMap = new HashMap<>();
        if (custom != null) {
            customHashMap.putAll(custom);
        }
        this.custom = customHashMap;
        return this;
    }

    @Override
    protected Endpoint<PNSetUUIDMetadataResult> createAction() {
        return new MappingEndpoint<>(
                pubnub.setUUIDMetadata(
                        uuid,
                        name,
                        externalId,
                        profileUrl,
                        email,
                        custom,
                        includeCustom,
                        type,
                        status
                ),
                result -> new PNSetUUIDMetadataResult(
                        result.getStatus(),
                        PNUUIDMetadata.from(result.getData())
                )
        );
    }
}
