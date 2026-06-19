package com.pubnub.docs.logging;
// https://www.pubnub.com/docs/sdks/java/logging#log-content-configuration

import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.logging.LogContentConfig;

public class LogContentConfigApp {
    public static void main(String[] args) throws Exception {
        // snippet.logContentConfigDefault
        PNConfiguration config = PNConfiguration.builder(new UserId("demoUserId"), "demo")
                .publishKey("demo")
                .logContentConfig(new LogContentConfig(1000, 4000))
                .build();

        PubNub pubnub = PubNub.create(config);
        // snippet.end

        // snippet.logContentConfigSuppressMessages
        PNConfiguration configSuppressMessages = PNConfiguration.builder(new UserId("demoUserId"), "demo")
                .publishKey("demo")
                .logContentConfig(new LogContentConfig(
                        0,
                        LogContentConfig.DEFAULT_LOGGED_HTTP_RESPONSE_MAX_BYTES
                ))
                .build();

        PubNub pubnubSuppressMessages = PubNub.create(configSuppressMessages);
        // snippet.end

        // snippet.logContentConfigUnlimitedResponse
        PNConfiguration configUnlimitedResponse = PNConfiguration.builder(new UserId("demoUserId"), "demo")
                .publishKey("demo")
                .logContentConfig(new LogContentConfig(
                        LogContentConfig.DEFAULT_LOGGED_MESSAGE_CONTENT_MAX_BYTES,
                        Integer.MAX_VALUE
                ))
                .build();

        PubNub pubnubUnlimitedResponse = PubNub.create(configUnlimitedResponse);
        // snippet.end
    }
}
