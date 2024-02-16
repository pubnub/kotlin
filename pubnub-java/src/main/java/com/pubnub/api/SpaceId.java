package com.pubnub.api;

import kotlin.text.StringsKt;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;


public class SpaceId {

    @Getter
    private final String value;

    public SpaceId(@NotNull String value) {
        if (StringsKt.isBlank(value)) {
            throw new IllegalArgumentException("SpaceId can't be null or empty");
        }
        this.value = value;
    }
}
