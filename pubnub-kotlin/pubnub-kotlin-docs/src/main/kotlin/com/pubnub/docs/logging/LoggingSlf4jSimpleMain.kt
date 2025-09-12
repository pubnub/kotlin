package com.pubnub.docs.logging
// https://www.pubnub.com/docs/sdks/kotlin/logging#example-usage-of-slf4j-simple

// snippet.loggingSlf4jSimpleMain
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LoggingSlf4jSimple {
    private val logger: Logger = LoggerFactory.getLogger(LoggingSlf4jSimple::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val config = PNConfiguration.builder(
            UserId("slf4jSimpleDemoUser"),
            "demo" // Replace with your Subscribe Key from the PubNub Admin Portal
        ) {
            // Add publish key (only required if publishing)
            publishKey = "demo" // Replace with your Publish Key from the PubNub Admin Portal
        }

        // Initialize PubNub with the configuration
        val pubnub = PubNub.create(config.build())

        // Perform some operations to generate logs
        try {
            // Get time operation
            logger.info("Executing time operation")
            pubnub.time().sync()
            logger.info("Time operation completed")

            // Publish a message
            logger.info("Publishing a message")
            pubnub.channel("slf4j-simple-demo-channel")
                .publish("Hello from SLF4J Simple Example")
                .sync()
            logger.info("Message published successfully")
        } catch (e: PubNubException) {
            logger.error("Error during operations: ${e.message}", e)
        }

        logger.info("SLF4J Simple example complete - check the console output for details")
    }
}
// snippet.end
