package com.pubnub.docs;

import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;

public abstract class SnippetBase {
    protected PubNub createPubNub() {
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        return pubnub;
    }
}
