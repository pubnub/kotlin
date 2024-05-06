package com.pubnub.internal.endpoints.objects_api.channel;

import com.pubnub.api.endpoints.objects_api.channel.UpdateChannelMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import com.pubnub.api.utils.Optional;
import com.pubnub.api.utils.OptionalKt;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import kotlin.Unit;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class UpdateChannelMetadataImpl
        extends DelegatingEndpoint<PNChannelMetadataResult, PNSetChannelMetadataResult> implements UpdateChannelMetadata {

    public UpdateChannelMetadataImpl(final String channel, final PubNubCore pubnubInstance) {
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
    private Optional<String> description = Optional.none();

    @Setter
    private Optional<String> name = Optional.none();

    private Optional<Map<String, Object>> custom = Optional.none();

    @Setter
    private Optional<String> status = Optional.none();

    @Setter
    private Optional<String> type = Optional.none();

    @Setter
    private boolean includeCustom;

    @Override
    public UpdateChannelMetadata custom(@NotNull Optional<? extends Map<String, ?>> custom) {
        final HashMap<String, Object> customHashMap = new HashMap<>();
        OptionalKt.onValue(custom, stringObjectMap -> {
            customHashMap.putAll(stringObjectMap);
            this.custom = Optional.of(customHashMap);
            return Unit.INSTANCE;
        });
        OptionalKt.onAbsent(custom, () -> {
            this.custom = Optional.none();
            return Unit.INSTANCE;
        });
        return this;
    }

    @AllArgsConstructor
    public static class Builder implements UpdateChannelMetadata.Builder {
        private final PubNubCore pubnubInstance;

        @Override
        public @NotNull UpdateChannelMetadataImpl channel(final @NotNull String channel) {
            return new UpdateChannelMetadataImpl(channel, pubnubInstance);
        }
    }
}
