package com.pubnub.docs.logging;
// https://www.pubnub.com/docs/sdks/java/logging#implement-logging-using-logback

// snippet.loggingLogbackApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingLogbackApp {
    // Get an SLF4j logger instance (named after the class)
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingLogbackApp.class);

    public static void main(String[] args) throws PubNubException {
        LOGGER.info("Starting PubNub logging example...");

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
        // in logback.xml configure specific logger for PubNub network calls e.g.
        // <logger name="pubnub.okhttp" level="DEBUG"/>

        // Other optional configurations
        configBuilder.secure(true);

        // Initialize PubNub with the configuration
        PubNub pubnub = PubNub.create(configBuilder.build());

        LOGGER.info("PubNub client initialized with BODY level logging");

        // Perform some operations to generate logs
        // Get time operation will generate logs
        LOGGER.info("Executing time operation...");
        pubnub.time().sync();
        LOGGER.info("Time operation executed - check logs for details");

        LOGGER.info("Publishing message...");
        pubnub.channel("logging-demo-channel")
                .publish("Hello from Logging Example")
                .sync();
        LOGGER.info("Message published - check logs for details");

        LOGGER.info("Logging example complete - detailed logs should be visible in your console output");
    }
}