package com.pubnub.docs.logging.customLogger;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;

import java.util.Collections;

public class CustomLoggerApp {
    public static void main(String[] args) throws PubNubException {
        // snippet.customLoggerConfiguration

        // Configure SDK with custom logger
        PNConfiguration config = PNConfiguration.builder(new UserId("demoUserId"), "subscribeKey")
                .customLoggers(Collections.singletonList(new MonitoringLogger()))
                .build();

        PubNub pubnub = PubNub.create(config);

        // snippet.end
    }
}
