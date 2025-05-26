package com.pubnub.docs.logging;
// https://www.pubnub.com/docs/sdks/java/logging#example-usage-of-slf4j-simple
//snippet.loggingSlf4jSimpleApp

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingSlf4jSimpleApp {
    // Get an SLF4J logger instance (named after the class)
    private static final Logger logger = LoggerFactory.getLogger(LoggingSlf4jSimpleApp.class);

    public static void main(String[] args) throws PubNubException {
        // Configure PubNub with logging enabled
        logger.info("Initializing PubNub with logging enabled");

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(
                new UserId("slf4jSimpleDemoUser"),
                "demo" // Replace with your Subscribe Key from the PubNub Admin Portal
        );

        // Add publish key (only required if publishing)
        configBuilder.publishKey("demo"); // Replace with your Publish Key from the PubNub Admin Portal

        // Set log verbosity to BODY to see detailed logs
        configBuilder.logVerbosity(PNLogVerbosity.BODY);

        // Initialize PubNub with the configuration
        PubNub pubnub = PubNub.create(configBuilder.build());

        logger.info("PubNub client initialized with BODY level logging");

        // Perform some operations to generate logs
        // Get time operation
        logger.info("Executing time operation");
        pubnub.time().sync();
        logger.info("Time operation completed");

        // Publish a message
        logger.info("Publishing a message");
        pubnub.channel("slf4j-simple-demo-channel")
                .publish("Hello from SLF4J Simple Example")
                .sync();
        logger.info("Message published successfully");

        logger.info("SLF4J Simple example complete - check the console output for details");
    }
}
// snippet.end
