package com.pubnub.api.models.consumer;

import lombok.Data;

public interface PNPage {
    String getHash();

    static Next next(String hash) {
        if (hash != null) {
            return new Next(hash);
        } else {
            return null;
        }
    }

    @Data
    class Next implements PNPage {
        private final String hash;
    }
}
