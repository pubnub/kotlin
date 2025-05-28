package com.pubnub.docs.logging;
// https://www.pubnub.com/docs/sdks/java/logging#how-to-enable-logging

// snippet.loggingExampleApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;

public class LoggingExampleApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub with logging enabled
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(
                new UserId("loggingDemoUser"),
                "demo" // Replace with your Subscribe Key from the PubNub Admin Portal
        );

        // Add publish key (only required if publishing)
        configBuilder.publishKey("demo"); // Replace with your Publish Key from the PubNub Admin Portal

        // Set log verbosity - options are:
        // NONE - Turn off logging
        // BODY - Log request and response bodies
        configBuilder.logVerbosity(PNLogVerbosity.BODY);

        // Other optional configurations
        configBuilder.secure(true);

        // Initialize PubNub with the configuration
        PubNub pubnub = PubNub.create(configBuilder.build());

        System.out.println("PubNub client initialized with BODY level logging");

        // Perform some operations to generate logs
        // Get time operation will generate logs
        pubnub.time().sync();
        System.out.println("Time operation executed - check logs for details");

        // Publish a message to generate more logs
        pubnub.channel("logging-demo-channel")
                .publish("Hello from Logging Example")
                .sync();
        System.out.println("Message published - check logs for details");


        System.out.println("Logging example complete - detailed logs should be visible in your console output");
    }
}
// snippet.end
