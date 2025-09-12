package com.pubnub.docs.configuration
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#basic-usage
// snippet.configurationMain
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.PNConfiguration

fun main() {
    println("PubNub Configuration Example")
    println("============================")

    // Create a basic configuration
    val basicConfig = createBasicConfig()
    println("\n1. Basic Configuration (Required Parameters)")
    printConfigDetails(basicConfig)

    // Create an advanced configuration
    val advancedConfig = createAdvancedConfig()
    println("\n2. Advanced Configuration (With Optional Parameters)")
    printConfigDetails(advancedConfig)

    // Create a configuration with encryption
    val encryptedConfig = createEncryptedConfig()
    println("\n3. Configuration with Encryption")
    printConfigDetails(encryptedConfig)

    println("\nNote: In a real application, you would use:")
    println("val pubnub = PubNub.create(config)")
    println("to create the PubNub instance with your configuration.")
}

/**
 * Creates a basic PubNub configuration with only the required parameters
 */
fun createBasicConfig(): PNConfiguration {
    val userId = UserId("basic-user-id")
    val builder = PNConfiguration.builder(userId, "demo").apply {
        // The publishKey is required if you want to publish messages
        publishKey = "demo"

        // These are the minimum required parameters to initialize PubNub
    }
    return builder.build()
}

/**
 * Creates an advanced PubNub configuration with various optional parameters
 */
fun createAdvancedConfig(): PNConfiguration {
    val userId = UserId("advanced-user-id")
    val builder = PNConfiguration.builder(userId, "demo").apply {
        // Basic parameters
        publishKey = "demo"

        // Connection parameters
        connectTimeout = 10 // Connection timeout in seconds
        subscribeTimeout = 310 // Subscribe request timeout in seconds
        nonSubscribeReadTimeout = 10 // Timeout for non-subscribe operations

        // Security parameter
        secure = true // Enable TLS (true by default)

        // Presence parameters
        presenceTimeout = 300 // How long the server considers client alive for presence
        heartbeatInterval = 150 // How often client sends heartbeat signals
        suppressLeaveEvents = false // Whether to send leave events when disconnecting

        // Debugging parameters
        customLoggers = listOf(MyCustomLoggerImpl()) // Add custom loggers if needed

        // Retry configuration (for reconnection)
        retryConfiguration = RetryConfiguration.Linear(
            delayInSec = 3, // Retry with fixed delay of 3 seconds
            maxRetryNumber = 5 // Maximum 5 retry attempts
        )

        // Notification options
        heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
    }
    return builder.build()
}

/**
 * When config options of slf4j implementation of your choice like logback or log4j don't meet you logging requirements implement CustomLogger and pass implementation to PNConfiguration.customLoggers
 */
class MyCustomLoggerImpl : CustomLogger {
    override val name: String = "MyCustomLoggerImpl"

    override fun trace(message: String?) {
        stringMessages.add(message ?: "")
    }

    override fun trace(message: LogMessage) {
        logMessages.add(message)
    }

    override fun debug(message: String?) {
        stringMessages.add(message ?: "")
    }

    override fun debug(logMessage: LogMessage) {
        logMessages.add(logMessage)
    }

    companion object {
        val stringMessages = mutableListOf<String>()
        val logMessages = mutableListOf<LogMessage>()

        fun clear() {
            stringMessages.clear()
            logMessages.clear()
        }
    }
}

/**
 * Creates a PubNub configuration with encryption enabled
 */
fun createEncryptedConfig(): PNConfiguration {
    val userId = UserId("encrypted-user-id")
    val builder = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"

        // Enable message encryption with 256-bit AES-CBC (recommended)
        cryptoModule = CryptoModule.createAesCbcCryptoModule("my-secret-key")

        // Alternatively, for legacy compatibility:
        // cryptoModule = CryptoModule.createLegacyCryptoModule("my-secret-key")
    }
    return builder.build()
}

/**
 * Prints the details of a configuration object
 */
fun printConfigDetails(config: PNConfiguration) {
    println("  • User ID: ${config.userId}")
    println("  • Subscribe Key: ${config.subscribeKey}")
    println("  • Publish Key: ${config.publishKey}")
    println("  • Secure: true (default)")

    // Determine if encryption is enabled
    val encryptionEnabled = config.cryptoModule != null
    if (encryptionEnabled) {
        println("  • Encryption: Enabled")
    } else {
        println("  • Encryption: Disabled")
    }

    // Note: We can't access all configuration properties directly
    // as some are private within the PNConfiguration object
}
// snippet.end
