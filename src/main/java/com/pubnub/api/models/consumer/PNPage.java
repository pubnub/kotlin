package com.pubnub.api.models.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class PNPage {
    @Getter
    protected final String hash;

    public static Next next(String hash) {
        return new Next(hash);
    }

    public static Previous previous(String hash) {
        return new Previous(hash);
    }

    public static class Next extends PNPage {
        Next(String hash) {
            super(hash);
        }
    }

    public static class Previous extends PNPage {
        Previous(String hash) {
            super(hash);
        }
    }
}
