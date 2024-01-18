package com.pubnub.api;

import com.pubnub.api.builder.PubNubErrorBuilder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;


public class SpaceId {

    @Getter
    private final String value;

    public SpaceId(@NotNull String value) {
        if (value != null && !value.isEmpty()) {
            throw PubNubRuntimeException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SPACEID_NULL_OR_EMPTY).build();
        };
        this.value = value;
    }
}
