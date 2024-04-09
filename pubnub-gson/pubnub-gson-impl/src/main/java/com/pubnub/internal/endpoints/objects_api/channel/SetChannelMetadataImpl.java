package com.pubnub.internal.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.objects_api.channel.SetChannelMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetChannelMetadataImpl
        extends DelegatingEndpoint<PNChannelMetadataResult, PNSetChannelMetadataResult> implements SetChannelMetadata {

    public SetChannelMetadataImpl(final String channel, final PubNubCore pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNSetChannelMetadataResult> mapResult(@NotNull ExtendedRemoteAction<PNChannelMetadataResult> action) {
        return new MappingRemoteAction<>(action, pnChannelMetadataResult ->
                new PNSetChannelMetadataResult(
                        pnChannelMetadataResult.getStatus(),
                        PNChannelMetadata.from(pnChannelMetadataResult.getData())
                )
        );
    }

    @Override
    @NotNull
    protected EndpointInterface<PNChannelMetadataResult> createAction() {
        return pubnub.setChannelMetadata(
                channel,
                name,
                description,
                custom,
                includeCustom,
                type,
                status
        );
    }

    private final String channel;

    @Setter
    private String description;

    @Setter
    private String name;

    private Map<String, Object> custom;

    @Setter
    private String status;

    @Setter
    private String type;

    @Setter
    private boolean includeCustom;

    @Override
    public SetChannelMetadata custom(Map<String, Object> custom) {
        final HashMap<String, Object> customHashMap = new HashMap<>();
        if (custom != null) {
            customHashMap.putAll(custom);
        }
        this.custom = customHashMap;
        return this;
    }

    @AllArgsConstructor
    public static class Builder implements SetChannelMetadata.Builder {
        private final PubNubCore pubnubInstance;

        @Override
        public SetChannelMetadata channel(final String channel) {
            return new SetChannelMetadataImpl(channel, pubnubInstance);
        }
    }
}
