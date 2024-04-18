package com.pubnub.internal.endpoints.objects_api.uuid;

import com.pubnub.api.endpoints.objects_api.uuid.SetUserMetadata;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction;
import com.pubnub.api.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.utils.Optional;
import com.pubnub.api.utils.OptionalKt;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataResult;
import kotlin.Unit;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class SetUserMetadataImpl extends DelegatingEndpoint<PNUUIDMetadataResult, PNSetUUIDMetadataResult> implements SetUserMetadata {
    @Setter
    private String uuid;
    @Setter
    private Optional<String> name = Optional.none();
    @Setter
    private Optional<String> externalId = Optional.none();
    @Setter
    private Optional<String> profileUrl = Optional.none();
    @Setter
    private Optional<String> email = Optional.none();

    private Optional<Map<String, ?>> custom = Optional.none();
    @Setter
    private boolean includeCustom;
    @Setter
    private Optional<String> type = Optional.none();
    @Setter
    private Optional<String> status = Optional.none();

    public SetUserMetadataImpl(final PubNubCore pubnub) {
        super(pubnub);
    }

    @NotNull
    @Override
    public SetUserMetadata custom(@NotNull Optional<? extends Map<String, ?>> custom) {
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

    @Override
    @NotNull
    protected EndpointInterface<PNUUIDMetadataResult> createAction() {
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
