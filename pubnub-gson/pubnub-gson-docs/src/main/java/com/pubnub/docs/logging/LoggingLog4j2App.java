package com.pubnub.docs.logging;
// https://www.pubnub.com/docs/sdks/java/logging#implement-logging-using-log4j-2
//snippet.loggingLog4j2App

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingLog4j2App {
    // Get a Log4j2 logger instance (named after the class)
    private static final Logger logger = LoggerFactory.getLogger(LoggingLog4j2App.class);

    public static void main(String[] args) throws PubNubException {
        // Configure PubNub with logging enabled
        logger.info("Initializing PubNub with Log4j2 logging enabled");

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(
                new UserId("log4j2DemoUser"),
                "demo" // Replace with your Subscribe Key from the PubNub Admin Portal
        );

        // Add publish key (only required if publishing)
        configBuilder.publishKey("demo"); // Replace with your Publish Key from the PubNub Admin Portal

        // Initialize PubNub with the configuration
        PubNub pubnub = PubNub.create(configBuilder.build());

        logger.info("PubNub client initialized with Log4j2 logging");

        // Perform some operations to generate logs
        // Get time operation
        logger.info("Executing time operation");
        pubnub.time().sync();
        logger.info("Time operation completed");

        // Publish a message
        logger.info("Publishing a message");
        pubnub.channel("log4j2-demo-channel")
                .publish("Hello from Log4j2 Example")
                .sync();
        logger.info("Message published successfully");

        logger.info("Log4j2 example complete - check the log file for details");
    }
}
// snippet.end