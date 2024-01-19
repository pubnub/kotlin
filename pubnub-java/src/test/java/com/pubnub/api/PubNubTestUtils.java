package com.pubnub.api;

import com.pubnub.internal.PNConfiguration;

public class PubNubTestUtils {

    public static PNConfiguration getInternalConfigOf(com.pubnub.api.PNConfiguration config) {
        return config.getInternalConfig();
    }

    public static PubNub getApiPubNubOf(com.pubnub.api.PNConfiguration config, com.pubnub.internal.PubNub pubNubImpl) {
        return new PubNub(config, pubNubImpl);
    }
}
