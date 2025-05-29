package com.pubnub.docs.logging
// https://www.pubnub.com/docs/sdks/kotlin/logging#implement-logging-using-logback-classic

// snippet.loggingLogbackMain
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LoggingLogback {
    // Get an SLF4j logger instance (named after the class)
    private val logger: Logger = LoggerFactory.getLogger(LoggingLogback::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("Starting PubNub logging example...")

        // Configure PubNub with logging enabled
        val config = PNConfiguration.builder(
            UserId("loggingDemoUser"),
            "demo" // Replace with your Subscribe Key from the PubNub Admin Portal
        ) {
            // Add publish key (only required if publishing)
            publishKey = "demo" // Replace with your Publish Key from the PubNub Admin Portal

            // Set log verbosity - options are:
            // NONE - Turn off logging
            // BODY - Log request and response bodies
            logVerbosity = PNLogVerbosity.BODY
            // in logback.xml configure specific logger for PubNub network calls e.g.
            // <logger name="pubnub.okhttp" level="DEBUG"/>

            // Other optional configurations
            secure = true
        }

        // Initialize PubNub with the configuration
        val pubnub = PubNub.create(config.build())

        logger.info("PubNub client initialized with BODY level logging")

        // Perform some operations to generate logs
        try {
            // Get time operation will generate logs
            logger.info("Executing time operation...")
            pubnub.time().sync()
            logger.info("Time operation executed - check logs for details")

            logger.info("Publishing message...")
            pubnub.channel("logging-demo-channel")
                .publish("Hello from Logging Example")
                .sync()
            logger.info("Message published - check logs for details")

        } catch (e: PubNubException) {
            logger.error("Error during operations: {}", e.message, e)
        }

        logger.info("Logging example complete - detailed logs should be visible in your console output")
    }
}
// snippet.end
