package com.pubnub.docs.logging

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object LoggingLog4j2 {
    private val logger: Logger = LogManager.getLogger(LoggingLog4j2::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        // Configure PubNub with logging enabled
        logger.info("Initializing PubNub with logging enabled")

        val config = PNConfiguration.builder(
            UserId("log4jDemoUser"),
            "demo" // Replace with your Subscribe Key from the PubNub Admin Portal
        ) {
            // Add publish key (only required if publishing)
            publishKey = "demo" // Replace with your Publish Key from the PubNub Admin Portal
            // Set log verbosity to BODY to see detailed logs
            logVerbosity = PNLogVerbosity.BODY
            // in log4j2.xml configure specific logger for PubNub network calls e.g.
            // <Logger name="pubnub.okhttp" level="debug" additivity="false">
            //     <AppenderRef ref="Console"/>
            // </Logger>
        }

        // Initialize PubNub with the configuration
        val pubnub = PubNub.create(config.build())

        logger.info("PubNub client initialized with BODY level logging")

        // Perform some operations to generate logs
        try {
            // Get time operation
            logger.info("Executing time operation")
            pubnub.time().sync()
            logger.info("Time operation completed")

            // Publish a message
            logger.info("Publishing a message")
            pubnub.channel("log4j-demo-channel")
                .publish("Hello from Log4j Example")
                .sync()
            logger.info("Message published successfully")
        } catch (e: PubNubException) {
            logger.error("Error during operations: ${e.message}", e)
        }

        logger.info("Log4j example complete - check the log file for details")
    }
}
