package com.pubnub.api;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;


public class SpaceId {

    @Getter
    private final String value;


    public SpaceId(@NotNull String value) {
// TODO fix merge
//        PubNubUtil.require(value != null && !value.isEmpty(), PNERROBJ_SPACEID_NULL_OR_EMPTY);
        this.value = value;
    }
}
