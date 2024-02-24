package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetChannelMetadata
        extends DelegatingEndpoint<PNSetChannelMetadataResult> {

    public SetChannelMetadata(final String channel, final InternalPubNubClient pubnubInstance) {
        super(pubnubInstance);
        this.channel = channel;
    }

    @Override
    protected ExtendedRemoteAction<PNSetChannelMetadataResult> createAction() {
        return new MappingRemoteAction<>(
                pubnub.setChannelMetadata(
                        channel,
                        name,
                        description,
                        custom,
                        includeCustom,
                        type,
                        status
                ), pnChannelMetadataResult ->
                new PNSetChannelMetadataResult(
                        pnChannelMetadataResult.getStatus(),
                        PNChannelMetadata.from(pnChannelMetadataResult.getData())
                )
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

    public SetChannelMetadata custom(Map<String, Object> custom) {
        final HashMap<String, Object> customHashMap = new HashMap<>();
        if (custom != null) {
            customHashMap.putAll(custom);
        }
        this.custom = customHashMap;
        return this;
    }

    public static Builder builder(final InternalPubNubClient pubnubInstance) {
        return new Builder(pubnubInstance);
    }

    @AllArgsConstructor
    public static class Builder implements BuilderSteps.ChannelStep<SetChannelMetadata> {
        private final InternalPubNubClient pubnubInstance;

        @Override
        public SetChannelMetadata channel(final String channel) {
            return new SetChannelMetadata(channel, pubnubInstance);
        }
    }

}
